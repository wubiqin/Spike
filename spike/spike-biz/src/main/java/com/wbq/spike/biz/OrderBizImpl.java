package com.wbq.spike.biz;

import com.wbq.spike.api.OrderService;
import com.wbq.spike.constant.CodeConstant;
import com.wbq.spike.constant.RedisConstant;
import com.wbq.spike.exception.StateCodeException;
import com.wbq.spike.po.Order;
import com.wbq.spike.po.Stock;
import com.wbq.spike.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 19 九月 2018
 *  
 */
@Service
public class OrderBizImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderBizImpl.class);
    /**
     * retry times
     */
    private final int try_time = 10;

    @Resource
    private com.wbq.spike.service.OrderService orderService;

    @Resource
    private StockService stockService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createOrder(int sid) throws StateCodeException {
        logger.info("createOrder sid={}", sid);
        //read and check stock
        Stock stock = checkStock(sid);

        stockService.updateById(sid);

        return insertOrder(sid, stock);
    }

    /**
     * 不可重复读下读取不到其他事务提交到的数据，修改为read——committed
     *
     * @param sid stock id
     * @return 0 1
     * @throws StateCodeException exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public int createOrderOptimistic(int sid) throws StateCodeException {
        logger.info("createOrderOptimistic sid={}", sid);
        //read and check stock
        Stock stock = checkStock(sid);

        updateStockOptimistic(sid, stock);

        return insertOrder(sid, stock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public int createOrderOptimisticByRedis(int sid) throws StateCodeException {
        String countKey = RedisConstant.STOCK_COUNT + sid;
        String saleKey = RedisConstant.STOCK_SALE + sid;
        String versionKey = RedisConstant.STOCK_VERSION + sid;
        String nameKey = RedisConstant.STOCK_NAME + sid;
        if (redisTemplate.hasKey(countKey)) {
            logger.info("read stock from redis");
            return checkAndReadFromRedis(sid, countKey, saleKey, versionKey, nameKey);
        }
        //read from db and check stock
        Stock stock = checkStock(sid);
        updateStockOptimistic(sid, stock);
        //check insert order
        int result = checkInsertOrder(sid, stock);
        if (redisTemplate.hasKey(countKey)) {
            updateStockInRedis(countKey, saleKey, versionKey);
        }
        setKeyInRedis(countKey, saleKey, versionKey, nameKey, stock);
        return result;
    }

    @Override
    public int createOrderOptimisticByRedisAndMQ(int sid) throws StateCodeException {
        return 0;
    }

    private Stock checkStock(int sid) {
        Stock stock = stockService.getById(sid);
        if (stock == null) {
            logger.error("not exist stock with id={}", sid);
            throw new StateCodeException(String.format("not exist stock with id :%d", sid), CodeConstant.FAIL);
        }
        if (stock.getCount() <= 0) {
            throw new StateCodeException(String.format("stock has been sold out sid:%d", sid), CodeConstant.FAIL);
        }
        return stock;
    }

    private Order buildOrder(int sid, Stock stock) {
        return new Order.Builder().name(stock.getName()).sid(sid).build();
    }

    private void updateStockOptimistic(int sid, Stock stock) {
        int count = stockService.updateByIdAndVersion(sid, stock.getVersion());
        if (count == 0) {
            for (int i = 0; i < try_time; i++) {
                stock = checkStock(sid);
                count = stockService.updateByIdAndVersion(sid, stock.getVersion());
                if (count == 1) {
                    return;
                }
            }
            if (count == 0) {
                throw new StateCodeException("optimistic update stock fail", CodeConstant.FAIL);
            }
        }
    }

    private int insertOrder(int sid, Stock stock) {
        Order order = buildOrder(sid, stock);
        return orderService.insert(order);
    }

    private int checkInsertOrder(int sid, Stock stock) {
        int count = insertOrder(sid, stock);
        if (count == 0) {
            throw new StateCodeException("fail to create order", CodeConstant.FAIL);
        }
        return count;
    }

    //todo use lua to guarantee atomic
    private int checkAndReadFromRedis(int sid, String countKey, String saleKey, String versionKey, String nameKey) {
        //read from redis
        int count = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(countKey)));
        int sale = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(saleKey)));
        int version = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(versionKey)));
        if (count <= 0) {
            //del key
            //List<String> keys = Lists.newArrayList(countKey, saleKey, versionKey, nameKey);
            //logger.info("delete key in redis key={}", keys);
            //redisTemplate.delete(keys);
            throw new StateCodeException(String.format("redis: stock has been sold out sid:%d count:%d", sid, count), CodeConstant.FAIL);
        }
        String name = redisTemplate.opsForValue().get(nameKey);
        Stock stock = new Stock.Builder().count(count).sale(sale).name(name).version(version).build();
        updateDbAndRedis(sid, countKey, saleKey, versionKey, stock);
        return checkInsertOrder(sid, stock);
    }

    private void updateDbAndRedis(int sid, String countKey, String saleKey, String versionKey, Stock stock) {
        //update stock in db
        updateStockOptimistic(sid, stock);
        //update stock in redis 数据库的锁阻断 这里不用加锁
        updateStockInRedis(countKey, saleKey, versionKey);
    }

    private void updateStockInRedis(String countKey, String saleKey, String versionKey) {
        redisTemplate.opsForValue().increment(countKey, -1);
        redisTemplate.opsForValue().increment(saleKey, 1);
        redisTemplate.opsForValue().increment(versionKey, 1);
    }

    private void setKeyInRedis(String countKey, String saleKey, String versionKey, String nameKey, Stock stock) {
        redisTemplate.opsForValue().setIfAbsent(countKey, stock.getCount() - 1 + "");
        redisTemplate.opsForValue().setIfAbsent(saleKey, stock.getSale() + 1 + "");
        redisTemplate.opsForValue().setIfAbsent(versionKey, stock.getVersion() + 1 + "");
        redisTemplate.opsForValue().setIfAbsent(nameKey, stock.getName());
        setExpireTime(countKey, saleKey, versionKey, nameKey);
    }

    private void setExpireTime(String countKey, String saleKey, String versionKey, String nameKey) {
        redisTemplate.expire(countKey, 30 * 60, TimeUnit.SECONDS);
        redisTemplate.expire(saleKey, 30 * 60, TimeUnit.SECONDS);
        redisTemplate.expire(versionKey, 30 * 60, TimeUnit.SECONDS);
        redisTemplate.expire(nameKey, 30 * 60, TimeUnit.SECONDS);
    }
}

package com.wbq.spike.biz;

import com.wbq.spike.api.OrderService;
import com.wbq.spike.constant.CodeConstant;
import com.wbq.spike.exception.StateCodeException;
import com.wbq.spike.po.Order;
import com.wbq.spike.po.Stock;
import com.wbq.spike.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createOrder(int sid) throws StateCodeException {
        logger.info("createOrder sid={}", sid);
        Stock stock = stockService.getById(sid);

        checkStock(stock, sid);

        stockService.updateById(sid);

        return insertOrder(sid, stock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createOrderOptimistic(int sid) throws StateCodeException {
        logger.info("createOrderOptimistic sid={}", sid);
        Stock stock = stockService.getById(sid);

        checkStock(stock, sid);

        updateStockOptimistic(sid, stock);

        return insertOrder(sid, stock);
    }

    @Override
    public int createOrderOptimisticByRedis(int sid) throws StateCodeException {
        return 0;
    }

    @Override
    public int createOrderOptimisticByRedisAndMQ(int sid) throws StateCodeException {
        return 0;
    }

    private void checkStock(Stock stock, int sid) {
        if (stock == null) {
            logger.error("not exist stock with id={}", sid);
            throw new StateCodeException(String.format("not exist stock with id :%d", sid), CodeConstant.FAIL);
        }
        if (stock.getCount() <= 0) {
            throw new StateCodeException(String.format("stock has been sold out sid:%d", sid), CodeConstant.FAIL);
        }
    }

    private Order buildOrder(int sid, Stock stock) {
        return new Order.Builder().name(stock.getName()).sid(sid).build();
    }

    private void updateStockOptimistic(int sid, Stock stock) {
        int count = stockService.updateByIdAndVersion(sid, stock.getVersion());
        if (count == 0) {
            for (int i = 0; i < try_time; i++) {
                count = stockService.updateByIdAndVersion(sid, stock.getVersion());
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
}

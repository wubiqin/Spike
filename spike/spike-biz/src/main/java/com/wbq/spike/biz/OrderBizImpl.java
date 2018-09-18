package com.wbq.spike.biz;

import com.wbq.spike.api.OrderService;
import com.wbq.spike.exception.StateCodeException;
import com.wbq.spike.po.Order;
import com.wbq.spike.po.Stock;
import com.wbq.spike.service.StockService;

import javax.annotation.Resource;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 19 九月 2018
 *  
 */
public class OrderBizImpl implements OrderService {

    @Resource
    private com.wbq.spike.service.OrderService orderService;

    @Resource
    private StockService stockService;

    @Override
    public int createOrder(int sid) throws StateCodeException {
        Stock stock = stockService.getById(sid);
        if (stock != null && stock.getCount() > 0) {
            stockService.updateById(sid);
            Order order = new Order.Builder().name(stock.getName()).sid(sid).build();
            return orderService.insert(order);
        }
        return 0;
    }

    @Override
    public int createOrderOptimistic(int sid) throws StateCodeException {
        Stock stock = stockService.getById(sid);
        if (stock != null && stock.getCount() > 0) {
            stockService.updateByIdAndVersion(sid, stock.getVersion());
            Order order = new Order.Builder().name(stock.getName()).sid(sid).build();
            return orderService.insert(order);
        }
        return 0;
    }

    @Override
    public int createOrderOptimisticByRedis(int sid) throws StateCodeException {
        return 0;
    }

    @Override
    public int createOrderOptimisticByRedisAndMQ(int sid) throws StateCodeException {
        return 0;
    }
}

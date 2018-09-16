package com.wbq.spike.api;

import com.wbq.spike.exception.StateCodeException;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 16 九月 2018
 *  
 */
public interface OrderService {
    /**
     * create order
     *
     * @param sid stock id
     * @return order id
     * @throws StateCodeException exception
     */
    int createOrder(int sid) throws StateCodeException;

    /**
     * create order in optimistic way by optimistic lock
     *
     * @param sid stock id
     * @return order id
     */
    int createOrderOptimistic(int sid) throws StateCodeException;

    /**
     * create order in optimistic way by redis
     *
     * @param sid stock id
     * @return order id
     * @throws StateCodeException exception
     */
    int createOrderOptimisticByRedis(int sid) throws StateCodeException;

    /**
     * create order in optimistic way by redis and asynchronous write order
     *
     * @param sid stock id
     * @return order id
     * @throws StateCodeException exception
     */
    int createOrderOptimisticByRedisAndMQ(int sid) throws StateCodeException;
}

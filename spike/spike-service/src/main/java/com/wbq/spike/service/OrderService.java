package com.wbq.spike.service;

import com.wbq.spike.po.Order;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 19 九月 2018
 *  
 */
public interface OrderService {

    /**
     * insert order
     *
     * @param order order
     * @return insert count
     */
    int insert(Order order);
}

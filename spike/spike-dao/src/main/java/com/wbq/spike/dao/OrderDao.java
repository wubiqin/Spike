package com.wbq.spike.dao;

import com.wbq.spike.po.Order;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 17 九月 2018
 *  
 */
public interface OrderDao {
    /**
     * insert order
     *
     * @param order order
     * @return insert count
     */
    int insert(Order order);
}

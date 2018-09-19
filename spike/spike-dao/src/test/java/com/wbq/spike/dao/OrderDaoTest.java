package com.wbq.spike.dao;

import com.wbq.spike.po.Order;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 19 九月 2018
 *  
 */
public class OrderDaoTest extends BaseTest {
    @Resource
    private OrderDao orderDao;

    @Test
    public void insert() {
        Order order = new Order.Builder().sid(1).name("1").build();
        orderDao.insert(order);
    }
}
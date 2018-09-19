package com.wbq.spike.service.impl;

import com.wbq.spike.dao.OrderDao;
import com.wbq.spike.po.Order;
import com.wbq.spike.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 19 九月 2018
 *  
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;

    @Override
    public int insert(Order order) {
        return orderDao.insert(order);
    }
}

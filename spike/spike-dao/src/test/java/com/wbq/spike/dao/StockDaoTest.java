package com.wbq.spike.dao;

import com.wbq.spike.po.Stock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 17 九月 2018
 *  
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring-dao.xml")
public class StockDaoTest {
    @Resource
    private StockDao stockDao;

    @Test
    public void insertSelective() {
        Stock stock=new Stock();
        stock.setName("iphone xs");
        stock.setSale(0);
        stock.setCount(100);
        int count=stockDao.insertSelective(stock);
        Assert.assertEquals(1,count);
    }
}
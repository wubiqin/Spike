package com.wbq.spike.dao;

import com.wbq.spike.po.Stock;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 17 九月 2018
 *  
 */
public class StockDaoTest extends BaseTest{
    @Resource
    private StockDao stockDao;

    @Test
    public void insertSelective() {
        Stock stock = new Stock.Builder().build();
        stock.setName("iphone xs");
        stock.setSale(0);
        stock.setCount(100);
        int count = stockDao.insertSelective(stock);
        Assert.assertEquals(1, count);
    }
}
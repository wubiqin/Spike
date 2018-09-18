package com.wbq.spike.service.impl;

import com.wbq.spike.dao.StockDao;
import com.wbq.spike.po.Stock;
import com.wbq.spike.service.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 19 九月 2018
 *  
 */
@Service("stockService")
public class StockServiceImpl implements StockService {

    @Resource
    private StockDao stockDao;

    @Override
    public int insertSelective(Stock stock) {
        return stockDao.insertSelective(stock);
    }

    @Override
    public int updateById(int id) {
        return stockDao.updateById(id);
    }

    @Override
    public int updateByIdAndVersion(int id, int version) {
        return stockDao.updateByIdAndVersion(id, version);
    }

    @Override
    public Stock getById(int id) {
        return stockDao.getById(id);
    }
}

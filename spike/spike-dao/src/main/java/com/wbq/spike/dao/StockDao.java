package com.wbq.spike.dao;

import com.wbq.spike.po.Stock;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 17 九月 2018
 *  
 */
public interface StockDao {
    /**
     * insert stock
     *
     * @param stock stock
     * @return 0:success 1:fail
     */
    int insertSelective(Stock stock);
}

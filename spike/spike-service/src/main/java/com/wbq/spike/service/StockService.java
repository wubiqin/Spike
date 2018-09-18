package com.wbq.spike.service;

import com.wbq.spike.po.Stock;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 19 九月 2018
 *  
 */
public interface StockService {

    /**
     * insert stock
     *
     * @param stock stock
     * @return 0:success 1:fail
     */
    int insertSelective(Stock stock);

    /**
     * update sale and count by id
     *
     * @param id id
     * @return update count
     */
    int updateById(int id);

    /**
     * update by id and version (optimistic lock)
     *
     * @param id      id
     * @param version version
     * @return update count
     */
    int updateByIdAndVersion(int id, int version);

    /**
     * get stock by id
     *
     * @param id id
     * @return stock
     */
    Stock getById(int id);
}

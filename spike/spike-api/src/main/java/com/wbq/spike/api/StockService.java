package com.wbq.spike.api;

import com.wbq.spike.exception.StateCodeException;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 16 九月 2018
 *  
 */
public interface StockService {
    /**
     * get current stock number
     *
     * @return stock number
     * @throws StateCodeException exception
     */
    int getStock() throws StateCodeException;
}

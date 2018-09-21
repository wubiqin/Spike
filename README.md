## 不加锁实现
        //read and check stock
        Stock stock = checkStock(sid);
        stockService.updateById(sid);
        return insertOrder(sid, stock);
会发生超卖

# 加乐观锁实现
 不可重复读下读取不到其他事务提交到的数据，修改为read——committed
 
        Stock stock = checkStock(sid);

        updateStockOptimistic(sid, stock);

        return insertOrder(sid, stock);
    
# redis加数据库乐观锁实现
        String countKey = RedisConstant.STOCK_COUNT + sid;
        String saleKey = RedisConstant.STOCK_SALE + sid;
        String versionKey = RedisConstant.STOCK_VERSION + sid;
        String nameKey = RedisConstant.STOCK_NAME + sid;
        if (redisTemplate.hasKey(countKey)) {
            logger.info("read stock from redis");
            return checkAndReadFromRedis(sid, countKey, saleKey, versionKey, nameKey);
        }
        //read from db and check stock
        Stock stock = checkStock(sid);
        updateStockOptimistic(sid, stock);
        //check insert order
        int result = checkInsertOrder(sid, stock);
        if (redisTemplate.hasKey(countKey)) {
            updateStockInRedis(countKey, saleKey, versionKey);
        }
        setKeyInRedis(countKey, saleKey, versionKey, nameKey, stock);
        return result;

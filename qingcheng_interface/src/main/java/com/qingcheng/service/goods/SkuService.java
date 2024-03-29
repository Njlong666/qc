package com.qingcheng.service.goods;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Sku;

import java.util.*;

/**
 * sku业务逻辑层
 */
public interface SkuService {


    public List<Sku> findAll();


    public PageResult<Sku> findPage(int page, int size);


    public List<Sku> findList(Map<String,Object> searchMap);


    public PageResult<Sku> findPage(Map<String,Object> searchMap,int page, int size);


    public Sku findById(String id);

    public void add(Sku sku);


    public void update(Sku sku);


    public void delete(String id);


    /**
     * 将商品价格存入Redis缓存中
     */
    public void savePaiceToRedis();


    /**
     * 根据skuId查询价格
     * @param id
     * @return
     */
    public Integer findPrice(String id);


    /**
     * 将价格保存到Redis缓存中
     * @param id
     * @param price
     */
    public void savePriceToRedisById(String id,Integer price);


    /**
     * 根据id将Redis价格的缓存删除
     */
    public void deletePriceFromRedis(String id);

}

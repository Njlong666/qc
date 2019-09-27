package com.qingcheng.service.order;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.order.Order;
import com.qingcheng.pojo.order.OrderItem;
import com.qingcheng.pojo.order.Order_OrderItem;

import java.util.*;

/**
 * order业务逻辑层
 */
public interface OrderService {


    public List<Order> findAll();


    public PageResult<Order> findPage(int page, int size);


    public List<Order> findList(Map<String,Object> searchMap);


    public PageResult<Order> findPage(Map<String,Object> searchMap,int page, int size);


    public Order findById(String id);

    public void add(Order order);


    public void update(Order order);


    public void delete(String id);

    /**
     * 查询订单和详情
     * @param orderId
     * @return
     */
    public Order_OrderItem findByOrderID(String orderId);

    /**
     * 批量发货
     * @param ordersList
     *
     */
    public void batchSend(List<Order> ordersList);


    /**
     * 订单超时
     *
     */
    public void orderTimeOutLogic();
}

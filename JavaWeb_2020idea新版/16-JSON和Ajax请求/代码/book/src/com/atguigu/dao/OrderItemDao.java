package com.atguigu.dao;

import com.atguigu.pojo.OrderItem;

import java.util.List;

public interface OrderItemDao {
    //保存订单细项
    public int saveOrderItem(OrderItem orderItem);

    //查询订单细项
    public List<OrderItem> queryOrderItemsByOrderId(String orderId);

}

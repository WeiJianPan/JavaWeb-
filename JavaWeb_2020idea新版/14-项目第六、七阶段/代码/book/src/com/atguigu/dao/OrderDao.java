package com.atguigu.dao;

import com.atguigu.pojo.Order;

import java.util.List;

public interface OrderDao {



    //添加订单到数据库中
    public int saveOrder(Order order);

    //查询所有订单
    public List<Order> queryOrders();

    //修改订单发货状态
    public int changeOrderStatus(String orderId, int status);

    public List<Order> queryUserOrders(Integer user_id);

}

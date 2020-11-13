package com.atguigu.service;

import com.atguigu.pojo.Cart;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderItem;

import java.util.List;

public interface OrderService {
    /**
     * @MethodName: 创建订单
     * @param: [cart, userId]
     * @Return: java.lang.String
     **/
    public String createOrder(Cart cart,Integer userId);


    /**
     * @MethodName: 查询所有订单
     * @param: []
     * @Return: java.util.List<com.learn.pojo.Order>
     **/
    public List<Order> showAllOrders();


    /**
     * @MethodName: 将订单状态改为发货
     * @param: [orderId]
     * @Return: int
     **/
    public int sendOrder(String orderId);

    /**
     * @MethodName: 查询指定用户的订单
     * @param: [user_id]
     * @Return: java.util.List<com.learn.pojo.Order>
     **/
    public List<Order> showUserOrders(Integer user_id);


    /**
     * @MethodName: 修改订单状态为签收
     * @param: [orderId]
     * @Return: int
     **/
    public int receiverOrder(String orderId);


    /**
     * @MethodName: 查询指定订单号的订单细项
     * @param: [orderId]
     * @Return: java.util.List<com.learn.pojo.OrderItem>
     **/
    public List<OrderItem> showOrderDetail(String orderId);
}

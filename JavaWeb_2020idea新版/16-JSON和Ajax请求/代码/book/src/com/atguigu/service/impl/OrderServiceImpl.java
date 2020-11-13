package com.atguigu.service.impl;

import com.atguigu.dao.BookDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderItemDao;
import com.atguigu.dao.impl.BookDaoImpl;
import com.atguigu.dao.impl.OrderDaoImpl;
import com.atguigu.dao.impl.OrderItemDaoImpl;
import com.atguigu.pojo.*;
import com.atguigu.service.OrderService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {


    private OrderDao orderDao = new OrderDaoImpl();
    private OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private BookDao bookDao = new BookDaoImpl();

    @Override
    public String createOrder(Cart cart, Integer userId) {
        /*订单表t_order操作 */
        // 订单号===唯一性
        String orderId = System.currentTimeMillis() + "" + userId;
        //创建一个订单对象
        Order order = new Order(orderId, new Date(), cart.getTotalPrice(), 0, userId);
        // 保存订单
        orderDao.saveOrder(order);

        /*订单表项t_order_item操作 */
        // 遍历购物车中每一个商品项转换成为订单项保存到数据库
        for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
            // 获取每一个购物车中的商品项
            CartItem cartItem = entry.getValue();

            // 转换为每一个订单项
            OrderItem orderItem = new OrderItem(null, cartItem.getName(), cartItem.getCount(), cartItem.getPrice(),
                    cartItem.getTotalPrice(), orderId);

            // 保存订单项到数据库
            orderItemDao.saveOrderItem(orderItem);

            // 更新库存和销量
            //获取订单的ID，返回Book对象
            Book book = bookDao.queryBookById(cartItem.getId());

            //设置book都对象的销量，计算方式为：获取当前book对象的销量 + 购物车中该商品的数量
            book.setSales(book.getSales() + cartItem.getCount());

            //设置book都对象的库存，计算方式为：获取当前book对象的库存 - 购物车中该商品的数量
            book.setStock(book.getStock() - cartItem.getCount());
            //调用update方法，将Boo对象更新。
            bookDao.updateBook(book);
        }
        //清空购物车
        cart.clear();

        return orderId;
    }

    /**
     * @MethodName: 获取所有订单数据
     * @param: []
     * @Return: java.util.List<com.learn.pojo.Order>
     **/
    @Override
    public List<Order> showAllOrders() {
        return  orderDao.queryOrders();
    }

    /**
     * @MethodName: 修改订单状态
     * @param: [orderId]
     * @Return: int
     **/
    @Override
    public int sendOrder(String orderId) {
        //传入订单编号和发货状态，数据库中发货状态为1
        return orderDao.changeOrderStatus(orderId,1);
    }


    /**
     * @MethodName: 查找指定用户的所有订单
     * @param: [id]
     * @Return: java.util.List<com.learn.pojo.Order>
     **/
    @Override
    public List<Order> showUserOrders(Integer id) {
        return orderDao.queryUserOrders(id);
    }


    /**
     * @MethodName: 用户确认签收订单
     * @param: [orderId]
     * @Return: void
     **/
    @Override
    public int receiverOrder(String orderId) {
        return orderDao.changeOrderStatus(orderId,2);
    }


    /**
     * @MethodName: 查询订单详情
     * @param: [orderId]
     * @Return: java.util.List<com.learn.pojo.OrderItem>
     **/
    @Override
    public List<OrderItem> showOrderDetail(String orderId) {
        return orderItemDao.queryOrderItemsByOrderId(orderId);
    }

}

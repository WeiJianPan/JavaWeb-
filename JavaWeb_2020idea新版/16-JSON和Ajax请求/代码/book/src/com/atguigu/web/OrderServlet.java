package com.atguigu.web;

import com.atguigu.pojo.Cart;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderItem;
import com.atguigu.pojo.User;
import com.atguigu.service.OrderService;
import com.atguigu.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderServlet extends BaseServlet {

    //获取Service层的对象
    private OrderService orderService = new OrderServiceImpl();

    /**
     * @MethodName: 创建订单
     * @param: [request, response]
     * @Return: void
     **/
    protected void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 先获取 Cart 购物车对象
        Cart cart = (Cart) request.getSession().getAttribute("cart");

        // 获取Session域中的user对象
        User user = (User) request.getSession().getAttribute("user");

        //判断user对象是空
        if(user == null){
            //请求转发到登录页面
            request.getRequestDispatcher("/pages/user/login.jsp").forward(request, response);
            //结束Servlet方法，不再执行下面的代码
            return;
        }

        //user对象不为空，获取userId
        Integer userId = user.getId();

        //调用 orderService.createOrder(Cart,UserId);生成订单
        String  orderId = orderService.createOrder(cart, userId);

        //将订单号保存到Session域中
        request.getSession().setAttribute("orderId",orderId);

        //跳到结算成功页面,使用重定向，防止表单被重复提交。
        response.sendRedirect(request.getContextPath() + "/pages/cart/checkout.jsp");
    }

    /**
     * @MethodName: 获取所有订单的方法
     * @param: [request, response]
     * @Return: void
     **/
    protected void showAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取Session域中的user对象
        User user = (User) request.getSession().getAttribute("user");

        //判断user如果不是管理员。
        if("admin".equals(user.getUsername())){
            //调用Service来获取所有的订单项,管理员查看所有订单
            List<Order> orders = orderService.showAllOrders();
            //保存到request域中
            request.setAttribute("orders", orders);
            //转发到order_manager.jsp显示数据
            request.getRequestDispatcher("/pages/manager/order_manager.jsp").forward(request, response);
        }else{
            //其他用户查看自己的订单。
            List<Order> user_orders = orderService.showUserOrders(user.getId());
            //保存到request域中
            request.setAttribute("user_orders", user_orders);
            //转发到order_manager.jsp显示数据
            request.getRequestDispatcher("/pages/order/order.jsp").forward(request, response);
        }
    }

    /**
     * @MethodName: 修改订单状态，状态为1已发货。
     * @param: [request, response]
     * @Return: void
     **/
    protected void changeOrderStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取前台传来的
        String orderId = request.getParameter("orderId");

        //调用Service来修改指定ID的订单项
        orderService.sendOrder(orderId);

        //请求重定向到原来的页面。
        response.sendRedirect(request.getContextPath() + "/orderServlet?action=showAllOrders");
    }

    /**
     * @MethodName: 用户签收订单，状态为2
     * @param: [request, response]
     * @Return: void
     **/
    protected void receiverOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取前台传来的
        String orderId = request.getParameter("orderId");

        //调用Service将指定的订单ID修改状态为已签收。
        orderService.receiverOrder(orderId);

        //请求重定向到原来的页面。
        response.sendRedirect(request.getContextPath() + "/orderServlet?action=showAllOrders");
    }

    /**
     * @MethodName: 查看订单详情
     * @param: [request, response]
     * @Return: void
     **/
    protected void showOrderDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取前台传输的订单编号和域中的登录对象
        String orderId = request.getParameter("orderId");
        User user = (User) request.getSession().getAttribute("user");

        //如果是管理员，则跳到管理员查看详情页面。
        if("admin".equals(user.getUsername())){
            List<OrderItem> orderAdminDetails = orderService.showOrderDetail(orderId);
            request.setAttribute("orderAdminDetails",orderAdminDetails);
            request.getRequestDispatcher("/pages/manager/orderItem_manager.jsp").forward(request, response);
        }else{
            //否则就是用户查看详情页面
            List<OrderItem> orderUserDetails = orderService.showOrderDetail(orderId);
            request.setAttribute("orderUserDetails",orderUserDetails);
            request.getRequestDispatcher("/pages/order/orderItem_user.jsp").forward(request, response);
        }
    }

}

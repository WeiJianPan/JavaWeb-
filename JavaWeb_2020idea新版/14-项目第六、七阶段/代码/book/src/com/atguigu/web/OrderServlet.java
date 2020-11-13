package com.atguigu.web;

import com.atguigu.pojo.Cart;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.User;
import com.atguigu.service.OrderService;
import com.atguigu.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderServlet extends BaseServlet {

    private OrderService orderService = new OrderServiceImpl();

    /**
     * 生成订单
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void createOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 先获取Cart购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        // 获取Userid
        User loginUser = (User) req.getSession().getAttribute("user");

        if (loginUser == null) {
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req,resp);
            return;
        }

        Integer userId = loginUser.getId();
//        调用orderService.createOrder(Cart,Userid);生成订单
        String orderId = orderService.createOrder(cart, userId);

//        req.setAttribute("orderId", orderId);
        // 请求转发到/pages/cart/checkout.jsp
//        req.getRequestDispatcher("/pages/cart/checkout.jsp").forward(req, resp);

        req.getSession().setAttribute("orderId",orderId);

        resp.sendRedirect(req.getContextPath()+"/pages/cart/checkout.jsp");
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

}

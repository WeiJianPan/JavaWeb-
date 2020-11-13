package com.atguigu.web;

import com.atguigu.pojo.Book;
import com.atguigu.pojo.Cart;
import com.atguigu.pojo.CartItem;
import com.atguigu.service.BookService;
import com.atguigu.service.impl.BookServiceImpl;
import com.atguigu.utils.WebUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartServlet extends BaseServlet {
    private BookService bookService = new BookServiceImpl();

    /**
     * @MethodName: 加入购物车
     * @Return:
     **/
    protected void addItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求的参数  商品编号
        int bookId = WebUtils.parseInt(request.getParameter("id"), 0);

        //调用bookservice.queryBookById(id)，获取book对象得到图书的信息
        Book book = bookService.queryBookById(bookId);

        //把图书信息转换成为carItem商品项。
        CartItem cartItem = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());

        //调用cart.addItem，添加商品项
        //为了只存在一个cart对象，我们使用session来进行控制。
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute("cart", cart);
        }
        //添加商品项
        cart.addItem(cartItem);


        //重定向回原来商品所在的地址页面
        System.out.println(request.getHeader("Referer"));
        response.sendRedirect(request.getHeader("Referer"));

        //最后一个添加的商品名称
        request.getSession().setAttribute("lastName", cartItem.getName());
    }


    /**
     * @MethodName: 删除购物车中的商品项
     * @Return:
     **/
    protected void deleteItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取商品编号
        int id = WebUtils.parseInt(request.getParameter("id"), 0);

        //获取购物车对象
        Cart cart = (Cart) request.getSession().getAttribute("cart");

        if (cart != null) {
            // 删除 了购物车商品项
            cart.deleteItem(id);
            // 重定向回原来购物车展示页面
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

    /**
     * @MethodName: 清空购物车
     * @Return:
     **/
    protected void clearItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取购物车对象
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart != null) {
            //清空购物车
            cart.clear();
            // 重定向回原来购物车展示页面
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

    /**
     * @MethodName: 修改商品数量
     * @Return:
     **/

    protected void updateCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求的参数、商品编号、商品数量
        int id = WebUtils.parseInt(request.getParameter("id"), 0);
        int count = WebUtils.parseInt(request.getParameter("count"), 1);

        //获取cart购物车对象
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        //判断cart对象是否为空
        if (cart != null) {
            //修改商品数量。
            cart.updateCount(id, count);
            // 重定向回原来购物车展示页面
            response.sendRedirect(request.getHeader("Referer"));
        }
    }



    /**
     * @MethodName: Ajax的方式加入购物车
     * @Return:
     **/
    protected void ajaxAddItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求的参数  商品编号
        int bookId = WebUtils.parseInt(request.getParameter("id"), 0);

        //调用bookservice.queryBookById(id)，获取book对象得到图书的信息
        Book book = bookService.queryBookById(bookId);

        //把图书信息转换成为carItem商品项。
        CartItem cartItem = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());

        //调用cart.addItem，添加商品项
        //为了只存在一个cart对象，我们使用session来进行控制。
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute("cart", cart);
        }
        //添加商品项
        cart.addItem(cartItem);
        //最后一个添加的商品名称
        request.getSession().setAttribute("lastName", cartItem.getName());

        //返回购物车总的商品数量和最后一个添加的商品名称
        HashMap<String, Object> resultMap = new HashMap<>();
        //购物车总的商品数量
        resultMap.put("totalCount",cart.getTotalCount());
        //最后一个添加的商品名称
        resultMap.put("lastName",cartItem.getName());

        Gson gson = new Gson();
        //转换为Json字符串
        String json = gson.toJson(resultMap);

        response.getWriter().write(json);
    }
}

package com.atguigu.web;

import com.google.gson.*;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.service.impl.UserServiceImpl;
import com.atguigu.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

public class UserServlet extends BaseServlet {

    private UserService service = new UserServiceImpl();




    /*
     * @MethodName: 登录判断方法
     * @Return:
     **/
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取请求的参数
        /*String username = request.getParameter("username");
        String password = request.getParameter("password");*/

        //1.使用工具类中的参数注入实体类方法，将form表单中的请求参数都注入到实体类中。
        User user = WebUtils.copyParamToBean(request.getParameterMap(), new User());
        //2.调用userrService.login()登录处理业务
        User loginUser = service.login(user);

        //如果loginUser是null的话
        if (loginUser == null) {
            //将用户输入的错误信息和输入框的值回传给登录页面。
            request.setAttribute("msg", "用户名或密码输入不正确。");
            request.setAttribute("username", user.getUsername());
            //登录失败，跳回登录页面
            request.getRequestDispatcher("/pages/user/login.jsp").forward(request, response);

        } else {

            //将登录成功的用户信息保存到session中
            request.getSession().setAttribute("user", loginUser);
            //登录成功，跳转到成功界面
            request.getRequestDispatcher("/pages/user/login_success.jsp").forward(request, response);
        }

    }


    /**
     * @MethodName: 注销方法
     * @param: [request, response]
     * @Return: void
     **/
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1、 销毁 Session 中用户登录的信息（ 或者销毁 Session）
        request.getSession().invalidate();
        //2. 重定向到首页（ 或登录页面） 。
        response.sendRedirect(request.getContextPath());
    }


    /*
     * @MethodName: 注册判断方法
     * @Return:
     **/
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /* String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");*/
        String code = request.getParameter("code");

        //1.使用工具类中的参数注入实体类方法，将form表单中的请求参数都注入到实体类中。
        User user = WebUtils.copyParamToBean(request.getParameterMap(), new User());

        //获取验证码中的信息  KAPTCHA_SESSION_KEY就验证码唯一的key，通过这个key，就可以得到验证码的值 。
        String token = (String) request.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        //清空验证码信息
        request.getSession().removeAttribute(KAPTCHA_SESSION_KEY);

        //判断验证码是否正确
        if ( token != null&&token.equalsIgnoreCase(code)) {

            //判断数据库中是否已存在相同的用户名
            if (service.existsUsername(user.getUsername())) {
                System.out.println("用户名已存在。");

                //用户名已存在的情况，回传数据和错误消息
                request.setAttribute("msg", "用户名已存在。");
                //将用户输入的输入框的值回传给注册页面
                request.setAttribute("username", user.getUsername());
                request.setAttribute("email", user.getEmail());

                request.getRequestDispatcher("/pages/user/regist.jsp").forward(request, response);

            } else {
                //如果不存在相同用户名则添加数据,并跳转到注册成功页面
                service.registUser(user);
                request.getRequestDispatcher("/pages/user/regist_success.jsp").forward(request, response);
            }
        } else {
            //验证码错误跳回注册页面。
            System.out.println("验证码[" + code + "]错误");
            //将用户输入的输入框的值回传给注册页面
            request.setAttribute("username", user.getUsername());
            request.setAttribute("email", user.getEmail());

            //跳回注册页面。
            request.getRequestDispatcher("/pages/user/regist.jsp").forward(request, response);
        }
    }


    /**
     * @MethodName: Ajax判断用户名是否可用
     * @Return:
     **/
    public <Gson> void ajaxExistsUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取用户名
        String username = request.getParameter("username");

        //调用service层的判断用户名是否存在的方法
        boolean b = service.existsUsername(username);

        //new一个HashMap对象，用来存放回传的结果键值对。
        HashMap<String, Boolean> resultMap = new HashMap<>();
        resultMap.put("existsUsername", b);

        //实例化Gson对象，将结果转为Json
        Gson gson = new Gson();
        //这里他妈的！！一定要用小写！！！你妈的
        String json = gson.toJson(resultMap);

        //数据回传到前台。
        response.getWriter().write(json);
    }
}

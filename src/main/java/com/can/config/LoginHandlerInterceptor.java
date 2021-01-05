package com.can.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor/* implements HandlerInterceptor */{

   /* @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute("loginUser");

        if(loginUser==null){
            request.setAttribute("msg","没有权限，去登录");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        }

        return true;
    }*/

}

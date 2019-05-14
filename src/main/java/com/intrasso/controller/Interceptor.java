package com.intrasso.controller;

import com.intrasso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URL;

public class Interceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("interceptor");
        System.out.println(request.getRequestURL());
        URL url = new URL(request.getRequestURL().toString());
        System.out.println(url.getPath());
        HttpSession session = request.getSession();
        if(!url.getPath().equals("/index.html") && !url.getPath().equals("/login")) {
            if (session == null) {
                System.out.println("no session");
                response.sendRedirect("/");
                return true;
            }
            if (session.getAttribute("userId") == null) {
                System.out.println("no user id");
                response.sendRedirect("/");
                return true;
            }

            if (userRepository.getOne((Long) session.getAttribute("userId")) == null) {
                System.out.println("no registered user");
                response.sendRedirect("/");
                return true;
            }
        }
        return super.preHandle(request, response, handler);
    }
}

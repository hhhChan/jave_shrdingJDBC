package com.can.controller;

import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("/user/login")
    public String login(String username, String password, Model model, HttpSession session){

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            session.setAttribute("loginUser", username);
            System.out.println("验证成功");
            return "redirect:/main.html";
        }catch (UnknownAccountException e){
            model.addAttribute("msg","用户名错误");
            return "index.html";
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg","密码错误");
            return "index.html";
        }

    }

    @RequestMapping("/user/logout")
    public  String logOut(){

        return  "redirect:/index.html";
    }
}

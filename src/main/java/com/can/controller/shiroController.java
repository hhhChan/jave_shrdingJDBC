package com.can.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class shiroController {

    @RequestMapping("/noauth")
    @ResponseBody
    public String unthorized(){
        return "你太垃圾了，看不了";
    }
}

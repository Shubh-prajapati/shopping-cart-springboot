package com.ecom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/index")
    public String ShowIndex() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/base")
    public String base() {
        return "base";
    }

    @GetMapping("/product")
    public String product() {
        return "product";
    }
    @GetMapping("/view_product")
    public String view_product() {
        return "view_product";
    }
}

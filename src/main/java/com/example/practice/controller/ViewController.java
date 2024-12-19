package com.example.practice.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {


    @GetMapping("/")
    public String loginMenu() {
        return "loginMenu";
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

}

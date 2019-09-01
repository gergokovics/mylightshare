package com.mylightshare.main.com.mylightshare.main.controller;

import com.mylightshare.main.com.mylightshare.main.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserAccountController {


    @GetMapping("/login")
    public String login(Model model) {

        User user = new User();

        model.addAttribute("user", user);

        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticate() {

        return "home";
    }

    @GetMapping("/logout")
    public String logout() {

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/login";
    }

}

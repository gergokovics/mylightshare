package com.mylightshare.main.com.mylightshare.main.controller;


import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserFileService userFileService;

    public Controller(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @GetMapping("/userdata")
    public String index(Model model, Authentication auth) {

        List<UserFile> userFiles = userFileService.findAllByUsername(auth.getName());

        model.addAttribute("userFiles", userFiles);

        return "home";
    }

}

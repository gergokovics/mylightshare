package com.mylightshare.main.com.mylightshare.main.controller;


import com.mylightshare.main.com.mylightshare.main.dao.UserRepository;
import com.mylightshare.main.com.mylightshare.main.entity.User;
import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@org.springframework.stereotype.Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFileService userFileService;

    public AppController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @GetMapping("/")
    public String index(Model model, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName());

        List<UserFile> userFiles = userFileService.findByUserIdOrderByDownloadCountDesc(user.getId());

        model.addAttribute("popularUserFiles", userFiles.subList(0,8));

        userFiles = userFileService.findAllByUserId(user.getId());

        model.addAttribute("recentUserFiles", userFiles.subList(0, 12));

        return "home";
    }
}

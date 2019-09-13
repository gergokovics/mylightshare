package com.mylightshare.main.com.mylightshare.main.controller;


import com.mylightshare.main.com.mylightshare.main.dao.UserRepository;
import com.mylightshare.main.com.mylightshare.main.entity.User;
import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.modelattribute.UserFileModelAttribute;
import com.mylightshare.main.com.mylightshare.main.modelattribute.UserModelAttribute;
import com.mylightshare.main.com.mylightshare.main.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        int maxRecentUserFiles = 15;
        int recentDays = 3;

        User user = userRepository.findByUsername(auth.getName());

        List<UserFile> userFiles = userFileService.findByUserIdOrderByUploadedDesc(user.getId());
        List<UserFile> recentUserFiles = new ArrayList<>();

        for (UserFile userFile : userFiles) {

            boolean isRecent = userFile.getUploadTime().isAfter(LocalDateTime.now().minusDays(2));

            if (isRecent) {
                recentUserFiles.add(userFile);

                if (recentUserFiles.size() == maxRecentUserFiles)
                    break;
            }
        }


        List<UserFileModelAttribute> userFileModelAttributes = convertToAttributes(recentUserFiles);

        model.addAttribute("recentUserFiles", userFileModelAttributes);

        UserModelAttribute userModelAttribute = new UserModelAttribute(user);
        userModelAttribute.setPage("home");
        model.addAttribute("user", userModelAttribute);


        return "home";
    }

    private List<UserFileModelAttribute> convertToAttributes(List<UserFile> userFiles) {

        List<UserFileModelAttribute> userFileModelAttributes = new ArrayList<>();

        for (UserFile userFile : userFiles) {
            userFileModelAttributes.add(new UserFileModelAttribute(userFile));
        }

        return userFileModelAttributes;
    }
}

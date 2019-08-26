package com.mylightshare.main.com.mylightshare.main.rest;

import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.service.StorageService;
import com.mylightshare.main.com.mylightshare.main.service.UserFileService;
import com.mylightshare.main.com.mylightshare.main.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private StorageService storageService;

    @PutMapping("/urls/{id}")
    public String updateUserFileURL(@PathVariable int id) {

        UserFile userFile = userFileService.findById(id);

        String uniqueID = Generator.getUniqueID();
        String updatedUrl = Generator.getDownloadUrl(uniqueID);

        userFile.setUrlId(uniqueID);
        userFile.setUrl(updatedUrl);

        userFileService.save(userFile);

        return updatedUrl;
    }

    @DeleteMapping("/urls/{id}")
    public String removeUserFileURL(@PathVariable int id) {

        UserFile userFile = userFileService.findById(id);



        if (userFile != null) {
            userFile.setUrlId(null);
            userFile.setUrl(null);
            userFile.setPublic(false);

            userFileService.save(userFile);

            return "Removed UserFile URL with id: " + id;
        }

        return "Failed to remove URL";
    }

}

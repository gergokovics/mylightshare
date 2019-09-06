package com.mylightshare.main.com.mylightshare.main.controller;

import com.mylightshare.main.com.mylightshare.main.dao.UserRepository;
import com.mylightshare.main.com.mylightshare.main.entity.User;
import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.exception.StorageFileNotFoundException;
import com.mylightshare.main.com.mylightshare.main.service.StorageService;
import com.mylightshare.main.com.mylightshare.main.service.UserFileService;
import com.mylightshare.main.com.mylightshare.main.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Controller
public class FileController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/upload")
    public String listUploadedFiles(Model model, Authentication auth) {


        return "upload";

    }

    @GetMapping("/{urlId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String urlId) {

        UserFile userFile = userFileService.findByUrlId(urlId);

        Resource file = storageService.loadAsResource(userFile.getFilename());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + userFile.getOriginalFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   Authentication auth) {


        String uniqueID = Generator.getUniqueID();

        try {

            User user = userRepository.findByUsername(auth.getName());

            UserFile userFile = new UserFile();
            userFile.setUserId(user.getId());
            userFile.setOriginalFilename(file.getOriginalFilename());
            userFile.setFilename(uniqueID);
            userFile.setUploaded(LocalDateTime.now());

            userFile.setUrlId(uniqueID);
            userFile.setUrl(Generator.getDownloadUrl(uniqueID));

            userFileService.save(userFile);

            storageService.store(file, uniqueID);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user file");
        }



        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteUserFile(@PathVariable int id) {

        UserFile userFile = userFileService.findById(id);

        boolean deleted = storageService.delete(userFile.getFilename());

        if (deleted) {
            userFileService.deleteById(id);
            return "Successfully deleted file with id: " + id;
        }


        return "Failed to delete file";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}

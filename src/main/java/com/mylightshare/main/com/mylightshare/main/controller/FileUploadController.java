package com.mylightshare.main.com.mylightshare.main.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.exception.StorageFileNotFoundException;
import com.mylightshare.main.com.mylightshare.main.service.StorageService;
import com.mylightshare.main.com.mylightshare.main.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final UserFileService userFileService;

    @Autowired
    public FileUploadController(StorageService storageService, UserFileService userFileService) {
        this.storageService = storageService;
        this.userFileService = userFileService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model, Authentication auth) throws IOException {

        List<UserFile> files =
                userFileService.findAllByUsername(auth.getName());;

        model.addAttribute("files", files);

        return "uploadForm";

    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        UserFile userFile = userFileService.findByFilename(filename);

        Resource file = storageService.loadAsResource(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + userFile.getOriginalFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Authentication auth) {

        String uniqueID =
                NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 8);

        try {

            UserFile userFile = new UserFile();
            userFile.setUsername(auth.getName());
            userFile.setOriginalFilename(file.getOriginalFilename());
            userFile.setFilename(uniqueID);
            userFile.setUploaded(LocalDateTime.now());

            String UriString = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", uniqueID).toUriString();

            userFile.setUrl(UriString);

            userFileService.save(userFile);

            storageService.store(file, uniqueID);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user file");
        }

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");


        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}

package com.mylightshare.main.com.mylightshare.main.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String listUploadedFiles(Model model, Authentication auth) {

        List<UserFile> files =
                userFileService.findAllByUsername(auth.getName());

        model.addAttribute("files", files);

        return "uploadForm";

    }

    @GetMapping("/{urlId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String urlId) {

        UserFile userFile = userFileService.findByUrlId(urlId);

        Resource file = storageService.loadAsResource(userFile.getFilename());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + userFile.getOriginalFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Authentication auth) {

        String uniqueID = Generator.getUniqueID();

        try {

            UserFile userFile = new UserFile();
            userFile.setUsername(auth.getName());
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

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");


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

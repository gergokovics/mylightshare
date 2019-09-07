package com.mylightshare.main.com.mylightshare.main.controller;

import com.mylightshare.main.com.mylightshare.main.dao.UserRepository;
import com.mylightshare.main.com.mylightshare.main.entity.User;
import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.exception.StorageFileNotFoundException;
import com.mylightshare.main.com.mylightshare.main.formview.SortForm;
import com.mylightshare.main.com.mylightshare.main.service.StorageService;
import com.mylightshare.main.com.mylightshare.main.service.UserFileService;
import com.mylightshare.main.com.mylightshare.main.util.Generator;
import org.apache.commons.io.FilenameUtils;
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
import java.util.List;

@Controller
public class FileController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/upload")
    public String listUploadedFiles() {
        return "upload";
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

            serializeFile(userFile, file);

            userFileService.save(userFile);

            storageService.store(file, uniqueID);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user file");
        }



        return "redirect:/";
    }

    // Search file
    @GetMapping("/search")
    public String search(@RequestParam("term") String term, Model model, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName());

        List<UserFile> searchResult = userFileService.findAllByUserIdAndSerializedFilenameLike(user.getId(), "%" + term + "%");

        model.addAttribute("userFiles", searchResult);

        return "search";

    }

    // Sort files
    @GetMapping("/files")
    public String sort(@RequestParam(name="sort", required=false) String sort,
                       @RequestParam(name="order", required=false) String order,
                       Model model, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName());

        SortForm sortFeedback = new SortForm("upload-time", "desc");

        List<UserFile> userFiles;

        String sortAndOrderMode;
        if (sort != null && order != null) {
             sortAndOrderMode = sort + "-" + order;
             sortFeedback.setSort(sort);
             sortFeedback.setOrder(order);

        } else {
            sortAndOrderMode = "upload-time-desc";
        }

        // TODO implement cleaner way to sort
        switch (sortAndOrderMode) {
            case "upload-time-asc": {
                userFiles = userFileService.findByUserIdOrderByUploaded(user.getId());
                break;
            }
            case "upload-time-desc": {
                userFiles = userFileService.findByUserIdOrderByUploadedDesc(user.getId());
                break;
            }
            case "download-count-asc": {
                userFiles = userFileService.findByUserIdOrderByDownloadCount(user.getId());
                break;
            }
            case "download-count-desc": {
                userFiles = userFileService.findByUserIdOrderByDownloadCountDesc(user.getId());
                break;
            }
            case "last-download-asc": {
                userFiles = userFileService.findByUserIdOrderByLastDownload(user.getId());
                break;
            }
            case "last-download-desc": {
                userFiles = userFileService.findByUserIdOrderByLastDownloadDesc(user.getId());
                break;
            }
            default: {
                userFiles = userFileService.findAllByUserId(user.getId());
            }
        }


        model.addAttribute("userFiles", userFiles);
        model.addAttribute("sortForm", sortFeedback);

        return "files";
    }


    // Download file
    @GetMapping("/{urlId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String urlId) {

        UserFile userFile = userFileService.findByUrlId(urlId);

        Resource file = storageService.loadAsResource(userFile.getFilename());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + userFile.getSerializedFilename() + "\"").body(file);
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

    private void serializeFile(UserFile userFile, MultipartFile file) {
        List<UserFile> userFiles = userFileService.findAllByUserId(userFile.getUserId());
        String originalFilename, serializedFilename;
        serializedFilename = originalFilename = file.getOriginalFilename();

        if (!userFiles.isEmpty()) {
            int copyCounter = 0;
            for (UserFile existingUserFile : userFiles) {
                if (existingUserFile.getOriginalFilename().equals(originalFilename)) {
                    copyCounter++;
                }
            }

            if (copyCounter > 0) {
                userFile.setSerialNumber(copyCounter);

                serializedFilename =
                        FilenameUtils.getBaseName(originalFilename)
                                + " (" + copyCounter + ")"
                                + "." + FilenameUtils.getExtension(originalFilename);
            }

        }

        userFile.setSerializedFilename(serializedFilename);

    }

}

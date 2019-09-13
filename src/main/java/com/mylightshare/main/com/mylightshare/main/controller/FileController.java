package com.mylightshare.main.com.mylightshare.main.controller;

import com.mylightshare.main.com.mylightshare.main.dao.UserRepository;
import com.mylightshare.main.com.mylightshare.main.entity.User;
import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.exception.StorageFileNotFoundException;
import com.mylightshare.main.com.mylightshare.main.formview.SortForm;
import com.mylightshare.main.com.mylightshare.main.modelattribute.UserFileModelAttribute;
import com.mylightshare.main.com.mylightshare.main.modelattribute.UserModelAttribute;
import com.mylightshare.main.com.mylightshare.main.service.StorageService;
import com.mylightshare.main.com.mylightshare.main.service.UserFileService;
import com.mylightshare.main.com.mylightshare.main.util.Generator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public String listUploadedFiles(Model model, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName());

        UserModelAttribute userModelAttribute = new UserModelAttribute(user);
        userModelAttribute.setPage("upload");
        model.addAttribute("user", userModelAttribute);

        return "upload";
    }

    private List<UserFile> userFiles;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   Authentication auth) throws RuntimeException{

        String uniqueID = Generator.getUniqueID();


        try {

            User user = userRepository.findByUsername(auth.getName());

            // Check if user have enough storage space
            long storageSpaceLeft =  user.getStorageSpace() - file.getSize();

            if (storageSpaceLeft < 0) {

                long storageNeeded = Math.abs(storageSpaceLeft);

                String errorJsonMessage = "Insufficient storage space ("+Generator.formatFileSize(storageNeeded, 2)+" is needed).";

                return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE)
                        .header(HttpHeaders.CONTENT_TYPE, "text/plain").body(errorJsonMessage);


            } else {
                user.setStorageSpace(storageSpaceLeft);
            }

            UserFile userFile = new UserFile();
            userFile.setUserId(user.getId());

            userFile.setOriginalFilename(file.getOriginalFilename());
            userFile.setFilename(uniqueID);

            userFile.setSize(file.getSize());

            userFile.setUploadTime(LocalDateTime.now());

            userFile.setUrlId(uniqueID);
            userFile.setUrl(Generator.getDownloadUrl(uniqueID));

            serializeFile(userFile, file);

            user.incrementFileCount();

            userRepository.save(user);

            userFileService.save(userFile);

            storageService.store(file, uniqueID);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user file");
        }

        return ResponseEntity.ok("Successful upload");
    }

    // Search file
    @GetMapping("/search")
    public String search(@RequestParam("term") String term, Model model, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName());
        userFiles = userFileService.findAllByUserIdAndSerializedFilenameLike(user.getId(), "%" + term + "%");

        List<UserFileModelAttribute> userFileModelAttributes = convertToAttributes(userFiles);

        model.addAttribute("userFiles", userFileModelAttributes);

        UserModelAttribute userModelAttribute = new UserModelAttribute(user);
        userModelAttribute.setPage("files");
        model.addAttribute("user", userModelAttribute);

        return "search";

    }

    // Get files page
    @GetMapping("/files/page={pageNumber}")
    public String filePage(@PathVariable int pageNumber, Model model, Authentication auth) {

        pageNumber -= 1;

        final int elementPerPage = 15;

        int batchStartIndex = pageNumber * elementPerPage;
        int batchEndIndex = batchStartIndex + (elementPerPage);

        if (batchStartIndex > userFiles.size()) {
            return "file-card-end";
        }

        List<UserFile> userFilesBatch;
        if (userFiles.size() > batchEndIndex) {
            userFilesBatch = userFiles.subList(batchStartIndex, batchEndIndex);
        } else {
            userFilesBatch = userFiles.subList(batchStartIndex, userFiles.size());
        }

        List<UserFileModelAttribute> userFileModelAttributes = convertToAttributes(userFilesBatch);

        model.addAttribute("userFiles", userFileModelAttributes);

        User user = userRepository.findByUsername(auth.getName());

        UserModelAttribute userModelAttribute = new UserModelAttribute(user);
        userModelAttribute.setPage("files");
        model.addAttribute("user", userModelAttribute);

        return "file-card";
    }

    // Sort files
    @GetMapping("/files")
    public String sort(@RequestParam(name="sort", required=false) String sort,
                       @RequestParam(name="order", required=false) String order,
                       Model model, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName());

        SortForm sortFeedback = new SortForm("upload-time", "desc");

        String sortAndOrderMode;
        if (sort != null && order != null) {
             sortAndOrderMode = sort + "-" + order;
             sortFeedback.setSort(sort);
             sortFeedback.setOrder(order);

        } else {
            sortAndOrderMode = "upload-time-desc";
        }

        // TODO implement cleaner way to sort files
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

        List<UserFile> userFilesBatch;

        if (userFiles.size() > 15) {
            userFilesBatch = userFiles.subList(0, 15);
        } else {
            userFilesBatch = userFiles;
        }

        List<UserFileModelAttribute> userFileModelAttributes = convertToAttributes(userFilesBatch);

        model.addAttribute("userFiles", userFileModelAttributes);
        model.addAttribute("sortForm", sortFeedback);

        UserModelAttribute userModelAttribute = new UserModelAttribute(user);
        userModelAttribute.setPage("files");
        model.addAttribute("user", userModelAttribute);

        return "files";
    }


    // Download file
    @GetMapping("/download/{urlId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String urlId) {

        UserFile userFile = userFileService.findByUrlId(urlId);

        userFile.setLastDownload(LocalDateTime.now());

        userFile.incrementDownloadCount();

        userFileService.save(userFile);

        Resource file = storageService.loadAsResource(userFile.getFilename());

        User user = userRepository.findById(userFile.getUserId());

        user.incrementDownloadCount();

        userRepository.save(user);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + userFile.getSerializedFilename() + "\"").body(file);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteUserFile(@PathVariable int id) {

        UserFile userFile = userFileService.findById(id);

        boolean isDeleted = storageService.delete(userFile.getFilename());

        if (!isDeleted) {
            return "Failed to delete file";
        }

        userFileService.deleteById(id);

        // Free up storage space

        User user = userRepository.findById(userFile.getUserId());
        long updatedStorageSpace = user.getStorageSpace() + userFile.getSize();
        user.setStorageSpace(updatedStorageSpace);

        // Update user data
        user.decrementFileCount();

        userRepository.save(user);

        return "Successfully deleted file with id: " + id;



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

    private List<UserFileModelAttribute> convertToAttributes(List<UserFile> userFiles) {

        List<UserFileModelAttribute> userFileModelAttributes = new ArrayList<>();

        for (UserFile userFile : userFiles) {
            userFileModelAttributes.add(new UserFileModelAttribute(userFile));
        }

        return userFileModelAttributes;
    }

}

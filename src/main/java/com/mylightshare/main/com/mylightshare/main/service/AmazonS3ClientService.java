package com.mylightshare.main.com.mylightshare.main.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3ClientService {
    void uploadFileToS3Bucket(MultipartFile multipartFile, String fileName, boolean enablePublicReadAccess);

    boolean deleteFileFromS3Bucket(String fileName);

    Resource loadResourceFromS3Bucket(String fileName);
}

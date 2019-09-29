package com.mylightshare.main.com.mylightshare.main.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.mylightshare.main.com.mylightshare.main.util.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {

    private String awsS3AudioBucket;
    private AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    public AmazonS3ClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket)
    {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    }

    @Async
    public void uploadFileToS3Bucket(MultipartFile multipartFile, String fileName, boolean enablePublicReadAccess)
    {

        try {
            //creating the file in the server (temporarily)
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, "files/"+fileName, file);

            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            this.amazonS3.putObject(putObjectRequest);
            //removing the file created in the server
            file.delete();
        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
        }
    }

    @Async
    public boolean deleteFileFromS3Bucket(String fileName)
    {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, fileName));
            return true;
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
        }

        return false;
    }

    @Async
    public Resource loadResourceFromS3Bucket(String fileName)
    {
        try {
            S3Object fileObject = amazonS3.getObject(new GetObjectRequest(awsS3AudioBucket, "files/"+fileName));
            String objectUrl = Generator.getAmazonDownloadUrl(fileObject.getBucketName(), fileObject.getKey());

            return resourceLoader.getResource(objectUrl);
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while loading resource [" + fileName + "] ");
        }

        return null;
    }

}

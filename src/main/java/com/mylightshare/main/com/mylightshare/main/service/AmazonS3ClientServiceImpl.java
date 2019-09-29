package com.mylightshare.main.com.mylightshare.main.service;

import com.amazonaws.AmazonServiceException;
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

import java.io.IOException;

@Component
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {

    private String awsS3AudioBucket;
    private AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    public AmazonS3ClientServiceImpl(String awsS3AudioBucket, Region awsRegion)
    {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion.getName()).build();

        this.awsS3AudioBucket = awsS3AudioBucket;
    }

    @Async
    public void uploadFileToS3Bucket(MultipartFile multipartFile, String fileName, boolean enablePublicReadAccess)
    {

        try {

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(this.awsS3AudioBucket, "files/"+fileName, multipartFile.getInputStream(), objectMetadata);

            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);

            }
            this.amazonS3.putObject(putObjectRequest);

        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
            ex.printStackTrace();
        }
    }

    @Async
    public boolean deleteFileFromS3Bucket(String fileName)
    {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, "files/"+fileName));
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

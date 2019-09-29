package com.mylightshare.main.com.mylightshare.main.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.audio.bucket}")
    private String awsS3AudioBucket;

    @Bean(name = "awsRegion")
    public Region getAWSPollyRegion() {
        return Region.getRegion(Regions.fromName(awsRegion));
    }

    @Bean(name = "awsS3AudioBucket")
    public String getAWSS3AudioBucket() {
        return awsS3AudioBucket;
    }

}

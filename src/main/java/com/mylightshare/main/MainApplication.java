package com.mylightshare.main;

import com.mylightshare.main.com.mylightshare.main.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MainApplication {


	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);


	}

}

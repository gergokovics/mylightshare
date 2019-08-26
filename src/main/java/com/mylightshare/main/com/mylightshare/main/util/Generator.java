package com.mylightshare.main.com.mylightshare.main.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.mylightshare.main.com.mylightshare.main.controller.FileUploadController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public class Generator {

    public static String getUniqueID() {

        String uniqueID =
                NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 8);

        return uniqueID;
    }

    public static String getUniqueID(int size) {

        String uniqueID =
                NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, size);

        return uniqueID;
    }

    public static String getDownloadUrl() {

        String Url = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                "serveFile", getUniqueID()).toUriString();

        return Url;
    }

    public static String getDownloadUrl(String id) {

        String Url = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                "serveFile", id).toUriString();

        return Url;
    }
}

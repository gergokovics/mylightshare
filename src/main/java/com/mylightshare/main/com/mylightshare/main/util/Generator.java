package com.mylightshare.main.com.mylightshare.main.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.mylightshare.main.com.mylightshare.main.controller.FileController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        String Url = MvcUriComponentsBuilder.fromMethodName(FileController.class,
                "serveFile", getUniqueID()).toUriString();

        return Url;
    }

    public static String getDownloadUrl(String id) {

        String Url = MvcUriComponentsBuilder.fromMethodName(FileController.class,
                "serveFile", id).toUriString();

        return Url;
    }

    public static String formatFileSize(double bytes, int digits) {
        String[] dictionary = {"bytes", "KB", "MB", "GB"};
        int i = 0;
        for (i = 0; i < dictionary.length; i++) {
            if (bytes < 1024) {
                break;
            }
            bytes = bytes / 1024;
        }

        return String.format("%." + digits + "f", bytes) + " " + dictionary[i];
    }

    public static String formatDateTime(LocalDateTime dateTime) {

        if (dateTime != null)
        {
            return dateTime.format(DateTimeFormatter.ofPattern("YYYY.MM.dd. HH:mm"));
        } else {
            return "N/A";
        }

    }
}

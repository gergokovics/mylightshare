package com.mylightshare.main.com.mylightshare.main.modelattribute;

import com.mylightshare.main.com.mylightshare.main.entity.User;
import com.mylightshare.main.com.mylightshare.main.util.Generator;

public class UserModelAttribute {

    private String username;

    private String email;

    private String storageSpace;

    private int fileCount;

    private long downloadCount;

    private String page;

    public UserModelAttribute(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.storageSpace = Generator.formatFileSize(user.getStorageSpace(),2);
        this.fileCount = user.getFileCount();
        this.downloadCount = user.getDownloadCount();
        page = "home";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(String storageSpace) {
        this.storageSpace = storageSpace;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}

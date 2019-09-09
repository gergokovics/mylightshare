package com.mylightshare.main.com.mylightshare.main.modelattribute;

import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import com.mylightshare.main.com.mylightshare.main.util.Generator;

public class UserFileModelAttribute {

    private int id;

    private String filename;

    private String size;

    private String url;

    private String uploadTime;

    private String lastDownload;

    private int downloadCount;

    public UserFileModelAttribute(UserFile userFile){

        String fileSize = Generator.formatFileSize(userFile.getSize(), 3);
        String uploadTime = Generator.formatDateTime(userFile.getUploadTime());
        String lastDownload = Generator.formatDateTime(userFile.getLastDownload());

        this.id = userFile.getId();
        this.filename = userFile.getSerializedFilename();
        this.size = fileSize;
        this.url = userFile.getUrl();
        this.uploadTime = uploadTime;
        this.lastDownload = lastDownload;
        this.downloadCount = userFile.getDownloadCount();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getLastDownload() {
        return lastDownload;
    }

    public void setLastDownload(String lastDownload) {
        this.lastDownload = lastDownload;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
}

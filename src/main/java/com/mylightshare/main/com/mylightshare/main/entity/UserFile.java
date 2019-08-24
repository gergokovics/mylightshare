package com.mylightshare.main.com.mylightshare.main.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="files")
public class UserFile {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="username")
    private String username;

    @Column(name="filename")
    private String filename;

    @Column(name="path")
    private String path;

    @Column(name="uploaded")
    private LocalDateTime upload;

    @Column(name="last_download")
    private LocalDateTime lastDownload;

    @Column(name="download_count")
    private int downloadCount;

    public UserFile() {}

    public UserFile(int id, String username, String filename, String path, LocalDateTime upload, LocalDateTime lastDownload, int downloadCount) {
        this.id = id;
        this.username = username;
        this.filename = filename;
        this.path = path;
        this.upload = upload;
        this.lastDownload = lastDownload;
        this.downloadCount = downloadCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getUpload() {
        return upload;
    }

    public void setUpload(LocalDateTime upload) {
        this.upload = upload;
    }

    public LocalDateTime getLastDownload() {
        return lastDownload;
    }

    public void setLastDownload(LocalDateTime lastDownload) {
        this.lastDownload = lastDownload;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", filename='" + filename + '\'' +
                ", path='" + path + '\'' +
                ", upload=" + upload +
                ", lastDownload=" + lastDownload +
                ", downloadCount=" + downloadCount +
                '}';
    }
}

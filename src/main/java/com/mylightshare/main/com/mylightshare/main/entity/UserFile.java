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

    @Column(name="filename_original")
    private String originalFilename;

    @Column(name="filename")
    private String filename;

    @Column(name="url")
    private String url;

    @Column(name="public")
    private boolean isPublic;

    @Column(name="uploaded")
    private LocalDateTime uploaded;

    @Column(name="last_download")
    private LocalDateTime lastDownload;

    @Column(name="download_count")
    private int downloadCount = 0;

    public UserFile() {}

    public UserFile(int id, String username, String originalFilename, String filename, String url, boolean isPublic, LocalDateTime uploaded, LocalDateTime lastDownload, int downloadCount) {
        this.id = id;
        this.username = username;
        this.originalFilename = originalFilename;
        this.filename = filename;
        this.url = url;
        this.isPublic = isPublic;
        this.uploaded = uploaded;
        this.lastDownload = lastDownload;
        this.downloadCount = downloadCount;
    }

    public void incrementDownloadCount() {
        downloadCount++;
    }

    public void incrementDownloadCount(int i) {
        downloadCount += i;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getUploaded() {
        return uploaded;
    }

    public void setUploaded(LocalDateTime uploaded) {
        this.uploaded = uploaded;
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

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", originalFilename='" + originalFilename + '\'' +
                ", filename='" + filename + '\'' +
                ", URL='" + url + '\'' +
                ", isPublic=" + isPublic +
                ", upload=" + uploaded +
                ", lastDownload=" + lastDownload +
                ", downloadCount=" + downloadCount +
                '}';
    }
}
package com.mylightshare.main.com.mylightshare.main.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="files")
public class UserFile {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="user_id")
    private int userId;

    @Column(name="filename_original")
    private String originalFilename;

    @Column(name="filename")
    private String filename;

    @Column(name="url")
    private String url;

    @Column(name="url_id")
    private String urlId;

    @Column(name="public")
    private boolean isPublic;

    @Column(name="uploaded")
    private LocalDateTime uploaded;

    @Column(name="last_download")
    private LocalDateTime lastDownload;

    @Column(name="download_count")
    private int downloadCount = 0;

    public UserFile() {}

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", originalFilename='" + originalFilename + '\'' +
                ", filename='" + filename + '\'' +
                ", url='" + url + '\'' +
                ", urlId='" + urlId + '\'' +
                ", isPublic=" + isPublic +
                ", uploaded=" + uploaded +
                ", lastDownload=" + lastDownload +
                ", downloadCount=" + downloadCount +
                '}';
    }
}

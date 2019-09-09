package com.mylightshare.main.com.mylightshare.main.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="username")
    @Size(min=4, max=12, message="Username length must be in between 4 and 12 characters.")
    private String username;

    @Column(name="email")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="enabled")
    private boolean isEnabled;

    @Column(name="storage_space")
    private long storageSpace;

    @Column(name="file_count")
    private int fileCount;

    @Column(name="download_count")
    private long downloadCount;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public long getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(long bytes) {
        this.storageSpace = bytes;
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


    public void incrementFileCount() {
        this.fileCount++;
    }
    public void decrementFileCount() {
        this.fileCount--;
    }

    public void incrementDownloadCount() {
        this.downloadCount++;
    }
}

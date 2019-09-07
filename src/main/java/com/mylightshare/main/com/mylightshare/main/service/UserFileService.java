package com.mylightshare.main.com.mylightshare.main.service;

import com.mylightshare.main.com.mylightshare.main.entity.UserFile;

import java.util.List;

public interface UserFileService {

    UserFile findById(int id);

    UserFile findByUserId(int userId);

    UserFile findByFilename(String filename);

    UserFile findByUrlId(String urlId);

    List<UserFile> findAllByUserId(int userId);

    // Search files
    List<UserFile> findAllByUserIdAndSerializedFilenameLike(int userId, String searchValue);

    // Sort files
    List<UserFile> findByUserIdOrderByUploaded(int userId);
    List<UserFile> findByUserIdOrderByUploadedDesc(int userId);

    List<UserFile> findByUserIdOrderByDownloadCount(int userId);
    List<UserFile> findByUserIdOrderByDownloadCountDesc(int userId);

    List<UserFile> findByUserIdOrderByLastDownload(int userId);
    List<UserFile> findByUserIdOrderByLastDownloadDesc(int userId);

    void save(UserFile userFile);

    void deleteById(int id);
}

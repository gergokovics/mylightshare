package com.mylightshare.main.com.mylightshare.main.service;

import com.mylightshare.main.com.mylightshare.main.entity.UserFile;

import java.util.List;

public interface UserFileService {

    UserFile findById(int id);

    List<UserFile> findAllByUserId(int userId);

    UserFile findByUserId(int userId);

    UserFile findByFilename(String filename);

    UserFile findByUrlId(String urlId);

    void save(UserFile userFile);

    void deleteById(int id);
}

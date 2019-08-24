package com.mylightshare.main.com.mylightshare.main.service;

import com.mylightshare.main.com.mylightshare.main.entity.UserFile;

import java.util.List;

public interface UserFileService {

    List<UserFile> findAllByUsername(String user);

    UserFile findById(int id);

    void save(UserFile userFile);

    void deleteById(int id);
}

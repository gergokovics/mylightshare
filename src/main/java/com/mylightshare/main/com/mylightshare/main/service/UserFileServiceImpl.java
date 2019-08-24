package com.mylightshare.main.com.mylightshare.main.service;

import com.mylightshare.main.com.mylightshare.main.dao.UserFileRepository;
import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFileServiceImpl  implements UserFileService {

    private UserFileRepository userFileRepository;

    public UserFileServiceImpl(UserFileRepository userFileRepository) {
        this.userFileRepository = userFileRepository;
    }


    @Override
    public List<UserFile> findAllByUsername(String user) {

        return userFileRepository.findByUsername(user);
    }

    @Override
    public UserFile findById(int id) {

        Optional<UserFile> result = userFileRepository.findById(id);

        UserFile userFile = null;
        if (result.isPresent()) {
            userFile = result.get();
        } else {
            throw new RuntimeException("UserFile does not exist in the database with the id: " + id);
        }

        return userFile;
    }

    @Override
    public void save(UserFile userFile) {
        userFileRepository.save(userFile);
    }

    @Override
    public void deleteById(int id) {
        userFileRepository.deleteById(id);
    }
}

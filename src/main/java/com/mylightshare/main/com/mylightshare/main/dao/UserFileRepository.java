package com.mylightshare.main.com.mylightshare.main.dao;

import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFile, Integer> {

    List<UserFile> findByUsernameIgnoreCase(String username);

    Optional<UserFile> findByFilename(String filename);

    Optional<UserFile> findByUrlId(String urlId);

}

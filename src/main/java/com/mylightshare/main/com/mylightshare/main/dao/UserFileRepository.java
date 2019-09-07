package com.mylightshare.main.com.mylightshare.main.dao;

import com.mylightshare.main.com.mylightshare.main.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFile, Integer> {

    Optional<UserFile> findById(int id);

    Optional<UserFile> findByUserId(int userId);

    Optional<UserFile> findByFilename(String filename);

    Optional<UserFile> findByUrlId(String urlId);

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

}

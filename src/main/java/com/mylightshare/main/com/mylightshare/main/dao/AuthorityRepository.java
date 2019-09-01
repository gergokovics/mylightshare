package com.mylightshare.main.com.mylightshare.main.dao;

import com.mylightshare.main.com.mylightshare.main.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Authority findAllByUsername(String username);
}

package com.mylightshare.main.com.mylightshare.main.dao;

import com.mylightshare.main.com.mylightshare.main.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}

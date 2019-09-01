package com.mylightshare.main.com.mylightshare.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendEmail(MimeMessage mail) {
        javaMailSender.send(mail);
    }

    public MimeMessage createMimeMessage() {
        return javaMailSender.createMimeMessage();
    }

}

package com.spring.fastfood.service;

public interface EmailService {
    void sendMail (String to, String subject, String text);
     void sendEmailActive(String email, String activeCode);
}

package com.spring.fastfood.service.impl;

import com.spring.fastfood.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String FROM;

    @Override
    public void sendEmailActive(String email, String activeCode) {
        try {
            String subject = "Xác nhận kích hoạt tài khoản - FastFood";
            String url = "http://localhost:8080/auth/active?email=" + email + "&activeCode=" + activeCode;
            String text = "Vui lòng sử dụng mã sau để kích hoạt tài khoản: " + email
                    + "\nHoặc có thể sao chép đường dẫn này vào trình duyệt: " + url;
            sendMail(email, subject, text);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sending mail: " + e.getMessage());
        }
    }

    @Override
    public void sendMail(String to, String subject, String text) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(FROM);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
        } catch (Exception e) {
            log.error("errorMessage : {}", e.getMessage());
            throw new RuntimeException("Send mail fail");
        }
        javaMailSender.send(message);
    }
}

package com.spring.fastfood.controller;

import com.spring.fastfood.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/send-mail")
@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/")
    public String sendMail (){
        emailService.sendMail("duynv.it.052@gmail.com","Hello Duy", "Hello Duy");
        return "Success fully";
    }
}

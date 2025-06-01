package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.PaymentRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.PaymentResponse;
import com.spring.fastfood.service.PaymentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/list")
    public DataResponse<List<PaymentResponse>> getAllPayment (){
        return new DataResponse<>(HttpStatus.OK.value(), "get all payment",paymentService.getAllPayment());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<PaymentResponse> createPayment (@RequestBody PaymentRequest request){
        return new DataResponse<>(HttpStatus.CREATED.value(), "create payment",paymentService.createPayment(request));
    }

    @PutMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<PaymentResponse> updatePaymentById (@PathVariable long paymentId, @RequestBody PaymentRequest request){
        return new DataResponse<>(HttpStatus.ACCEPTED.value(), "update payment",paymentService.updatePaymentById(paymentId,request));
    }

    @DeleteMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<?> deletePaymentById (@PathVariable long paymentId){
        paymentService.deletePaymentById(paymentId);
        return new DataResponse<>(HttpStatus.NO_CONTENT.value(), "delete payment");
    }
}

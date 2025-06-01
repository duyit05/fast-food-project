package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.PaymentRequest;
import com.spring.fastfood.dto.response.PaymentResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.Payment;
import com.spring.fastfood.repository.PaymentRepository;
import com.spring.fastfood.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;


    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        Payment payment = Payment.builder()
                .paymentName(request.getPaymentName())
                .description(request.getDescription())
                .build();

        paymentRepository.save(payment);
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .paymentName(payment.getPaymentName())
                .description(payment.getDescription())
                .build();
    }

    @Override
    public List<PaymentResponse> getAllPayment() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(
                payment -> PaymentResponse.builder()
                        .paymentId(payment.getId())
                        .paymentName(payment.getPaymentName())
                        .description(payment.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public PaymentResponse updatePaymentById(long paymentId, PaymentRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("payment not found with id: " + paymentId));
        payment.setPaymentName(request.getPaymentName());
        payment.setDescription(request.getDescription());
        paymentRepository.save(payment);
        log.info("update payment: {}",payment);
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .paymentName(payment.getPaymentName())
                .description(payment.getDescription())
                .build();
    }

    @Override
    public void deletePaymentById(long paymentId) {
        paymentRepository.deleteById(paymentId);
        log.info("delete payment successfully");

    }
}

package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.PaymentRequest;
import com.spring.fastfood.dto.response.PaymentResponse;
import com.spring.fastfood.model.Payment;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment (PaymentRequest request);
    List<PaymentResponse> getAllPayment ();
    PaymentResponse updatePaymentById (long paymentId, PaymentRequest request);
    void deletePaymentById (long paymentId);
    Payment findPaymentById (long paymentId);
}

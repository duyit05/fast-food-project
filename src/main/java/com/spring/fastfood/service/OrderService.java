package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.OrderRequest;
import com.spring.fastfood.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder (OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrder(Long id, OrderRequest request);
    void deleteOrder(Long id);

    List<OrderResponse> userViewOrder ();
}

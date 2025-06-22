package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.OrderRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.OrderResponse;
import com.spring.fastfood.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    DataResponse<OrderResponse> createOrder (@Valid @RequestBody OrderRequest request){
        return new DataResponse<>(HttpStatus.CREATED.value(),"create order",orderService.createOrder(request));
    }

    @GetMapping("/get-order/{orderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    DataResponse<OrderResponse> getOrderById (@PathVariable long orderId){
        return new DataResponse<>(HttpStatus.CREATED.value(),"get order",orderService.getOrderById(orderId));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    DataResponse<List<OrderResponse>> getAllOrder (){
        return new DataResponse<>(HttpStatus.OK.value(),"get all order",orderService.getAllOrders());
    }

    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    DataResponse<?> deleteOrderById (@PathVariable long orderId){
        try {
            orderService.deleteOrder(orderId);
            return new DataResponse<>(HttpStatus.NO_CONTENT.value(), "delete order success");
        }catch (RuntimeException e){
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "delete order fail");
        }
    }
    @GetMapping("/my-order")
    @PreAuthorize("hasAuthority('USER')")
    DataResponse<List<OrderResponse>> userViewMyOrder (){
        return new DataResponse<>(HttpStatus.OK.value(),"view order",orderService.userViewOrder());
    }
}

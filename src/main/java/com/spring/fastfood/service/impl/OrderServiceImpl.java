package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.OrderDetailRequest;
import com.spring.fastfood.dto.request.OrderRequest;
import com.spring.fastfood.dto.response.OrderDetailResponse;
import com.spring.fastfood.dto.response.OrderResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.*;
import com.spring.fastfood.repository.FoodRepository;
import com.spring.fastfood.repository.OrderRepository;
import com.spring.fastfood.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final UserService userService;
    private final ShipService shipService;
    private final PaymentService paymentService;
    private final FoodService foodService;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        User user = userService.findByUsername(userService.getContextHolder());
        Ship ship = shipService.findShipById(request.getShipId());
        Payment payment = paymentService.findPaymentById(request.getPaymentId());

        Order order = new Order();
        order.setAddressReceive(request.getAddressReceive());
        order.setOrderDate(LocalDate.now());
        order.setPayment(payment);
        order.setShip(ship);
        order.setUser(user);

        Map<Long, OrderDetail> detailMap = new HashMap<>();

        // Gộp các món trùng (nếu có)
        for (OrderDetailRequest item : request.getFood()) {
            Food food = foodService.getFoodById(item.getFoodId());
            if (detailMap.containsKey(food.getId())) {
                OrderDetail existingDetail = detailMap.get(food.getId());
                existingDetail.setAmount(existingDetail.getAmount() + item.getAmount());
                existingDetail.setPrice(existingDetail.getPrice() + food.getPrice() * item.getAmount());
            } else {
                OrderDetail newDetail = new OrderDetail();
                newDetail.setOrder(order);
                newDetail.setFood(food);
                newDetail.setAmount(item.getAmount());
                newDetail.setPrice(food.getPrice() * item.getAmount());
                detailMap.put(food.getId(), newDetail);
            }
        }

        List<OrderDetail> orderDetails = new ArrayList<>(detailMap.values());
        double totalFood = 0;
        for (OrderDetail detail : orderDetails) {
            totalFood += detail.getPrice();
        }

        order.setOrderDetails(orderDetails);
        order.setTotalFood(totalFood);
        order.setShipPrice(ship.getShipPrice());
        order.setTotalPrice(totalFood + ship.getShipPrice());

        // Lưu đơn hàng cùng các chi tiết
        orderRepository.save(order);

        // Sau khi save, ID đã được sinh ra
        List<OrderDetailResponse> responses = new ArrayList<>();
        for (OrderDetail detail : order.getOrderDetails()) {
            OrderDetailResponse response = new OrderDetailResponse();
            response.setOrderDetailId(detail.getId());
            response.setFoodId(detail.getFood().getId());
            response.setAmount(detail.getAmount());
            response.setFoodName(detail.getFood().getFoodName());
            response.setUnitPrice(detail.getFood().getPrice());
            response.setPrice(detail.getPrice());
            responses.add(response);
        }

        // Trả về kết quả
        return OrderResponse.builder()
                .orderId(order.getId())
                .addressReceive(order.getAddressReceive())
                .dateOrder(order.getOrderDate())
                .shipPrice(order.getShipPrice())
                .totalFood(order.getTotalFood())
                .totalPrice(order.getTotalPrice())
                .shipName(order.getShip().getShipName())
                .paymentMethod(order.getPayment().getPaymentName())
                .orderDetails(responses)
                .build();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("can't found order with id: " + id));
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        for (OrderDetail item : order.getOrderDetails()) {
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setFoodId(item.getFood().getId());
            orderDetailResponse.setAmount(item.getAmount());
            orderDetailResponse.setFoodName(item.getFood().getFoodName());
            orderDetailResponse.setUnitPrice(item.getFood().getPrice());
            orderDetailResponse.setPrice(item.getPrice());
            orderDetailResponses.add(orderDetailResponse);
        }
        return OrderResponse.builder()
                .orderId(order.getId())
                .addressReceive(order.getAddressReceive())
                .dateOrder(order.getOrderDate())
                .shipPrice(order.getShipPrice())
                .totalFood(order.getTotalFood())
                .totalPrice(order.getTotalPrice())
                .shipName(order.getShip().getShipName())
                .paymentMethod(order.getPayment().getPaymentName())
                .orderDetails(orderDetailResponses)
                .build();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

            for (OrderDetail orderDetail : order.getOrderDetails()) {
                OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                orderDetailResponse.setOrderDetailId(orderDetail.getId());
                orderDetailResponse.setFoodId(orderDetail.getFood().getId());
                orderDetailResponse.setFoodName(orderDetail.getFood().getFoodName());
                orderDetailResponse.setAmount(orderDetail.getAmount());
                orderDetailResponse.setPrice(orderDetail.getPrice());
                orderDetailResponse.setUnitPrice(orderDetail.getFood().getPrice());
                orderDetailResponses.add(orderDetailResponse);
            }

            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(order.getId());
            orderResponse.setAddressReceive(order.getAddressReceive());
            orderResponse.setDateOrder(order.getOrderDate());
            orderResponse.setPaymentMethod(order.getPayment().getPaymentName());
            orderResponse.setShipName(order.getShip().getShipName());
            orderResponse.setShipPrice(order.getShipPrice());
            orderResponse.setTotalFood(order.getTotalFood());
            orderResponse.setTotalPrice(order.getTotalPrice());
            orderResponse.setOrderDetails(orderDetailResponses);

            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}

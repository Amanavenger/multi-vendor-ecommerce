package com.multi.vendor.orderservice.service;

import com.multi.vendor.orderservice.dto.OrderRequest;
import com.multi.vendor.orderservice.dto.OrderResponse;
import com.multi.vendor.orderservice.entity.Order;
import com.multi.vendor.orderservice.entity.OrderStatus;
import com.multi.vendor.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());

        //!- I will fetch the product price from productService using feign client, will implement this later in project

        order.setTotalAmount(ThreadLocalRandom.current().nextDouble(0.50, 1000));

        return  orderToResponse(orderRepository.save(order));
    }

    @Override
    public List<OrderResponse> orderByUser(Long UserId) {
        //!- I will fetch user from user-service using feign client will implement this later in project.

        List<Order> orders = orderRepository.findAllByUserId(UserId);

        List<OrderResponse> orderResponses = orders.stream().map(order -> orderToResponse(order)).collect(Collectors.toList());

        return orderResponses;
    }

    @Override
    public OrderResponse getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        return orderToResponse(order);
    }

    @Override
    public OrderResponse deleteOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.delete(order);
        return orderToResponse(order);
    }

    public OrderResponse orderToResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        orderResponse.setUserId(order.getUserId());
        orderResponse.setProductId(order.getProductId());
        orderResponse.setQuantity(order.getQuantity());
        orderResponse.setStatus(order.getStatus().toString());
        orderResponse.setOrderDate(order.getCreatedAt());

        return orderResponse;
    }
}

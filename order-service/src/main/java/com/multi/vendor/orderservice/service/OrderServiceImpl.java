package com.multi.vendor.orderservice.service;

import com.multi.vendor.orderservice.dto.OrderRequest;
import com.multi.vendor.orderservice.dto.OrderResponse;
import com.multi.vendor.orderservice.dto.UserResponse;
import com.multi.vendor.orderservice.entity.Order;
import com.multi.vendor.orderservice.entity.OrderStatus;
import com.multi.vendor.orderservice.exception.InvalidOrderIdException;
import com.multi.vendor.orderservice.exception.UserNotFoundInOrderServiceException;
import com.multi.vendor.orderservice.exception.UserNotOrderedException;
import com.multi.vendor.orderservice.feign.UserClient;
import com.multi.vendor.orderservice.repository.OrderRepository;
import feign.FeignException;
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
    private final UserClient userClient;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order();

        UserResponse userResponse;
        try {
            userResponse = userClient.getUser(orderRequest.getUserId()); // Feign call
        } catch (FeignException.NotFound ex) {
            throw new UserNotFoundInOrderServiceException("User not found with ID: " + orderRequest.getUserId());
        }

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
    public List<OrderResponse> orderByUser(Long userId) {
        //!- fetch user from user-service using feign client.
        UserResponse user;

        try {
            user = userClient.getUser(userId); // Feign call
        } catch (FeignException.NotFound ex) {
            throw new UserNotFoundInOrderServiceException("User not found with ID: " + userId);
        }

        if (user == null) {
            throw new UserNotFoundInOrderServiceException("User not found with ID: " + userId);
        }

        List<Order> orders = orderRepository.findAllByUserId(user.getId());

        if (orders.isEmpty()) {
            throw new UserNotOrderedException("User has not Order anything: ");
        }

        List<OrderResponse> orderResponses = orders.stream().map(order -> orderToResponse(order)).collect(Collectors.toList());

        return orderResponses;
    }

    @Override
    public OrderResponse getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new InvalidOrderIdException("Invalid order id: " +   orderId);
        }

        return orderToResponse(order);
    }

    @Override
    public OrderResponse deleteOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new InvalidOrderIdException("Invalid order id: " +   orderId);
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

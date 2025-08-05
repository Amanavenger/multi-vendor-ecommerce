package com.multi.vendor.orderservice.service;

import com.multi.vendor.orderservice.dto.OrderRequest;
import com.multi.vendor.orderservice.dto.OrderResponse;

import java.util.List;

public interface IOrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    List<OrderResponse> orderByUser(Long UserId);

    OrderResponse getOrderByOrderId(Long orderId);

    OrderResponse deleteOrderByOrderId(Long orderId);
}

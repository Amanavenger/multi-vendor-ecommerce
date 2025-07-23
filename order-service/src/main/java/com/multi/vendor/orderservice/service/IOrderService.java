package com.multi.vendor.orderservice.service;

import com.multi.vendor.orderservice.dto.OrderRequest;
import com.multi.vendor.orderservice.dto.OrderResponse;

import java.util.List;

public interface IOrderService {

    public OrderResponse createOrder(OrderRequest orderRequest);

    public List<OrderResponse> orderByUser(Long UserId);

    public OrderResponse getOrderByOrderId(Long orderId);

    public OrderResponse deleteOrderByOrderId(Long orderId);
}

package com.multi.vendor.orderservice.controller;

import com.multi.vendor.orderservice.dto.OrderRequest;
import com.multi.vendor.orderservice.dto.OrderResponse;
import com.multi.vendor.orderservice.service.IOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {


    private final IOrderService orderService;

    @GetMapping("/hello")
    public String hello() {
        return "This is Order Service";
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest orderRequest){
            OrderResponse orderResponse = orderService.createOrder(orderRequest);
            return new ResponseEntity<>(orderResponse,HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByUser(@Min(value = 1, message = "User ID must be greater than 0") @PathVariable Long userId){
        return new ResponseEntity<>(orderService.orderByUser(userId),HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@Min(value = 1, message = "Order ID must be greater than 0") @PathVariable Long orderId){
        return new ResponseEntity<>(orderService.getOrderByOrderId(orderId),HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderResponse> deleteOrderById(@Min(value = 1, message = "Order ID must be greater than 0") @PathVariable Long orderId){
        return new ResponseEntity<>(orderService.deleteOrderByOrderId(orderId),HttpStatus.OK);
    }

}

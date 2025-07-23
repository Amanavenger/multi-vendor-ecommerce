package com.multivendor.user_service.controller;

import com.multivendor.user_service.dto.LoginRequest;
import com.multivendor.user_service.dto.RegisterRequest;
import com.multivendor.user_service.dto.UserResponse;
import com.multivendor.user_service.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;



    @GetMapping("/hello")
    public String userHello() {
        return "Hello this is UserController";
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @GetMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.findUserById(id);
    }
}

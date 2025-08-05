package com.multivendor.user_service.service;

import com.multivendor.user_service.dto.LoginRequest;
import com.multivendor.user_service.dto.RegisterRequest;
import com.multivendor.user_service.dto.UserResponse;

public interface IUserService {

    UserResponse register(RegisterRequest registerRequest);

    UserResponse login(LoginRequest loginRequest);

    UserResponse findUserById(Long id);
}

package com.multivendor.user_service.service;

import com.multivendor.user_service.dto.LoginRequest;
import com.multivendor.user_service.dto.RegisterRequest;
import com.multivendor.user_service.dto.UserResponse;

public interface IUserService {

    public UserResponse register(RegisterRequest registerRequest);

    public UserResponse login(LoginRequest loginRequest);

    public UserResponse findUserById(Long id);
}

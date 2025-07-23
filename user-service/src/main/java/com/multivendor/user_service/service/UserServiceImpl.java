package com.multivendor.user_service.service;

import com.multivendor.user_service.dto.LoginRequest;
import com.multivendor.user_service.dto.RegisterRequest;
import com.multivendor.user_service.dto.UserResponse;
import com.multivendor.user_service.entity.Role;
import com.multivendor.user_service.entity.User;
import com.multivendor.user_service.exception.EmailAlreadyExistException;
import com.multivendor.user_service.exception.InvalidCredentialsException;
import com.multivendor.user_service.exception.UserNotFoundException;
import com.multivendor.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new EmailAlreadyExistException("Email already exists:😒😒😒 " + registerRequest.getEmail());
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.valueOf(registerRequest.getRole().toUpperCase()));
        userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public UserResponse login(LoginRequest loginRequest) {
       User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found with email 😑😑😑: " + loginRequest.getEmail()));

       if(!user.getPassword().equals(loginRequest.getPassword())) {
           throw new InvalidCredentialsException("Wrong Password 💀💀💀");
       }

       return mapToResponse(user);
    }

    @Override
    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id:😑😑😑 " + id));

        return mapToResponse(user);
    }


    public UserResponse mapToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole().toString());
        return userResponse;
    }
}

package com.niyogroup.taskmanagement.controller;

import com.niyogroup.taskmanagement.dto.request.LoginRequest;
import com.niyogroup.taskmanagement.dto.request.UserRequest;
import com.niyogroup.taskmanagement.dto.response.JWTAuthResponse;
import com.niyogroup.taskmanagement.dto.response.UserResponse;
import com.niyogroup.taskmanagement.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.registerUser(userRequest), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }


}

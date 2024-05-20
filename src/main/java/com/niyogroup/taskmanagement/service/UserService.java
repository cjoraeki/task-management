package com.niyogroup.taskmanagement.service;

import com.niyogroup.taskmanagement.dto.request.LoginRequest;
import com.niyogroup.taskmanagement.dto.request.UserRequest;
import com.niyogroup.taskmanagement.dto.response.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest userRequest);
    String loginUser(LoginRequest loginRequest);
}

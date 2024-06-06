package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.request.LoginRequest;
import com.dattran.ecommerceapp.dto.request.UserRequest;
import com.dattran.ecommerceapp.dto.response.UserResponse;
import com.dattran.ecommerceapp.entity.User;

import java.util.List;

public interface IUserService {
    public UserResponse createUser(UserRequest request);
    public String authenticate(LoginRequest loginRequest);
    UserResponse getUserDetailsFromToken(String token);
    List<UserResponse> getAllUsers();
    long countTotalUsers();
    long countTotalUsersByYear(int year);
}

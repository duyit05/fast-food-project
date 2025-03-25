package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.enums.UserStatus;

import java.util.List;

public interface UserService {
    UserResponse saveUser (UserRequest request);
    UserResponse updateUser (long userId , UserRequest request);
    void changeStatus (long userId, UserStatus status);
    void deleteUser (long userId);
    UserResponse getUserDetail (long userId);
    PageResponse<?> getAllUser (int pageNo , int pageSize, String sortBy,String keyword);
}

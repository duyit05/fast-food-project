package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.ChangePasswordRequest;
import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.request.UserUpdateRequest;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.dto.response.WishListResponse;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.model.User;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

    UserResponse updateUser (long userId , UserRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    void changeStatus (long userId, UserStatus status);
    void deleteUser (long userId);
    UserResponse getUserDetail (long userId);
    PageResponse<?> getAllUser (int pageNo , int pageSize, String sortBy,String keyword);

    UserResponse updateProfile(UserUpdateRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

     User getUserById(long userId);
     UserResponse viewMyInfo ();
     User findByUsername (String username);
     void changePasword (ChangePasswordRequest request);
}

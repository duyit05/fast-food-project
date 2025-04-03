package com.spring.fastfood.mapper;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.request.UserUpdateRequest;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserRequest request);
    UserResponse toUserResponse (User user);
    void updateUser(@MappingTarget User user, UserRequest request);
    void updateUserInfo(@MappingTarget User user, UserUpdateRequest request);
    List<UserResponse> toUserResponseList (List<User> users);

}

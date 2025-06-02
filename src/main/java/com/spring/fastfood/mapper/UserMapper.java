package com.spring.fastfood.mapper;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.request.UserUpdateRequest;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.model.User;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "avatar", ignore = true)
    User toUser (UserRequest request);
    UserResponse toUserResponse (User user);

    @Mapping(target = "avatar", ignore = true)
    void updateUser(@MappingTarget User user, UserRequest request);

    @Mapping(target = "avatar", ignore = true)
    void updateUserInfo(@MappingTarget User user, UserUpdateRequest request);
    List<UserResponse> toUserResponseList (List<User> users);
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate (@MappingTarget User entity , UserResponse dto);
}

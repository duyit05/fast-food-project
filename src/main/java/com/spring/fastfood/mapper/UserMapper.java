package com.spring.fastfood.mapper;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.request.UserUpdateRequest;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "multipartToString")
    User toUser (UserRequest request);
    UserResponse toUserResponse (User user);
    void updateUser(@MappingTarget User user, UserRequest request);
    void updateUserInfo(@MappingTarget User user, UserUpdateRequest request);
    List<UserResponse> toUserResponseList (List<User> users);
    @Named("multipartToString")
    static String mapAvatar(MultipartFile file) {
        return file != null ? file.getOriginalFilename() : null;
    }
}

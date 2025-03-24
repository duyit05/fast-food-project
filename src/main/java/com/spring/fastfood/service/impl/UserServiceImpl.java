package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.dto.response.ResponseData;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.User;
import com.spring.fastfood.repository.UserRepository;
import com.spring.fastfood.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public long saveUser(UserRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .status(request.getStatus())
                .dateOrBirth(request.getDateOrBirth())
                .build();
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public void updateUser(long userId, UserRequest request) {
        User user = getUserById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setStatus(request.getStatus());
        user.setDateOrBirth(request.getDateOrBirth());
        userRepository.save(user);
        log.error("User update successfully");
    }

    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);
        log.info("status changed");
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("user deleted , user id = {}", userId);
    }

    @Override
    public UserResponse getUserDetail(long userId) {
        User user = getUserById(userId);
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .status(user.getStatus())
                .dateOrBirth(user.getDateOrBirth())
                .build();
    }

    @Override
    public PageResponse<?> getAllUser(int pageNo, int pageSize, String sortBy) {
        int page = Math.max(pageNo - 1, 0); // Đảm bảo page không bị âm

        List<Sort.Order> sorts = new ArrayList<>();

        // Kiểm tra có điều kiện sort không
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                Sort.Direction direction = matcher.group(3).equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                sorts.add(new Sort.Order(direction, matcher.group(1)));
            }
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        Page<User> users = userRepository.findAll(pageable);

        List<UserResponse> response = users.getContent().stream().map(user ->
                UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .gender(user.getGender())
                        .status(user.getStatus())
                        .dateOrBirth(user.getDateOrBirth())
                        .build()
        ).toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(response)
                .build();
    }



    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find user id : " + userId));
    }
}

package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.dto.response.ResponseData;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.mapper.UserMapper;
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
import java.util.Arrays;
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
    private final UserMapper userMapper;

    @Override
    public UserResponse saveUser(UserRequest request) {
        User user = userMapper.toUser(request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(long userId, UserRequest request) {
        User user = getUserById(userId);
        userMapper.updateUser(user,request);
        return userMapper.toUserResponse(userRepository.save(user));
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
        return userMapper.toUserResponse(user);
    }

    @Override
    public PageResponse<?> getAllUser(int pageNo, int pageSize, String sortBy, String keyword) {
        // Trường hợp page không số âm
        int page = Math.max(pageNo - 1, 0);

        List<Sort.Order> sorts = new ArrayList<>();

        // Kiểm tra xem có điều kiện sort không
        if (StringUtils.hasLength(sortBy)) {
                                                    //   1       2     3
                                                    // firstName : asc | desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                Sort.Direction direction = matcher.group(3).equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC;
                sorts.add(new Sort.Order(direction, matcher.group(1)));
            }
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        // Chuyển từ khóa từ String thành List<String>
        List<String> keywords = Arrays.stream(keyword.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        Page<User> users = StringUtils.hasLength(keyword) ?
                userRepository.searchByKeywords(keywords, pageable) :
                userRepository.findAll(pageable);

        List<UserResponse> response = userMapper.toUserResponseList(users.getContent());

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

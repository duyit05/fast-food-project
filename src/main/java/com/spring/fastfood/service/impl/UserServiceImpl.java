package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.ChangePasswordRequest;
import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.request.UserUpdateRequest;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.exception.PasswordMismatchException;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.integration.MinioChannel;
import com.spring.fastfood.mapper.FoodMapper;
import com.spring.fastfood.mapper.UserMapper;
import com.spring.fastfood.model.User;
import com.spring.fastfood.repository.UserRepository;
import com.spring.fastfood.repository.WishListRepository;
import com.spring.fastfood.service.UserService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MinioChannel minioChannel;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse updateUser(long userId, UserRequest request) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        User user = getUserById(userId);
        if(request.getAvatar() != null && !request.getAvatar().isEmpty()){
            user.setAvatar(minioChannel.update(request.getAvatar()));
        }else {
            user.setAvatar(null);
        }
        userMapper.updateUser(user, request);
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
        List<String> keywords = StringUtils.hasLength(keyword)
                ? Arrays.stream(keyword.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList()
                : new ArrayList<>();


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

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find user id : " + userId));
    }

    @Override
    public UserResponse updateProfile(UserUpdateRequest request) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByUsername(username);
        if(request.getAvatar() != null && !request.getAvatar().isEmpty()){
            user.setAvatar(minioChannel.update(request.getAvatar()));
        }else {
            user.setAvatar(null);
        }
        userMapper.updateUserInfo(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse viewMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByUsername(username);
        return userMapper.toUserResponse(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("username not exist"));
    }

    @Override
    public void changePasword(ChangePasswordRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByUsername(username);
        if(!passwordEncoder.matches(request.getOldPassword(),user.getPassword())){
            throw new BadCredentialsException("old password incorrect");
        }
        if(!request.getNewPassword().equals(request.getNewPasswordRepeat())){
            throw new PasswordMismatchException("new password must match with new password repeat");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.response.DateResponse;
import com.spring.fastfood.dto.response.DataResponseError;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    public DateResponse<UserResponse> addUser(@Valid @RequestBody UserRequest request) {
        try {
            return new DateResponse<>(HttpStatus.CREATED.value(), "User added", userService.saveUser(request));
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "Create user fail");
        }
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DateResponse<UserResponse> updateUser(@PathVariable @Min(1) int userId, @Valid @RequestBody UserRequest request) {
        try {
            userService.updateUser(userId,request);
            return new DateResponse<>(HttpStatus.BAD_REQUEST.value(), "user update successfully");
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}" , e.getMessage() , e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "user update fail");
        }
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DateResponse<?> updateStatus(@Min(1) @PathVariable int userId,
                                        @RequestParam UserStatus status) {
        try {
            userService.changeStatus(userId,status);
            return new DateResponse<>(HttpStatus.BAD_REQUEST.value(), "user change status successfully");
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}" , e.getMessage() , e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "user change status fail");
        }
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DateResponse<?> deleteUser(@PathVariable @Min(value = 1,
                                      message = "userId must be greater than 0") int userId) {
        try {
            userService.deleteUser(userId);
            return new DateResponse<>(HttpStatus.BAD_REQUEST.value(), "user delete successfully");
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}" , e.getMessage() , e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "user delete fail");
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DateResponse<UserResponse> getUser(@PathVariable @Min(1) int userId) {
        try {
            return new DateResponse<>(HttpStatus.OK.value(), "user", userService.getUserDetail(userId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DateResponse<?> getAllUser(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam (defaultValue = "5")int pageSize,
            @RequestParam (required = false) String sortBy,
            @RequestParam(required = false) String keyword) {
        return new DateResponse<>(HttpStatus.OK.value(), "users", userService.getAllUser(pageNo, pageSize,sortBy,keyword));
    }
}

package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.request.UserUpdateRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.DataResponseError;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.dto.response.WishListResponse;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<UserResponse> updateUser(@PathVariable @Min(1) int userId, @Valid @RequestBody UserRequest request) {
        try {
            userService.updateUser(userId,request);
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "user update successfully");
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}" , e.getMessage() , e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "user update fail");
        }
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<?> updateStatus(@Min(1) @PathVariable int userId,
                                        @RequestParam UserStatus status) {
        try {
            userService.changeStatus(userId,status);
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "user change status successfully");
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}" , e.getMessage() , e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "user change status fail");
        }
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<?> deleteUser(@PathVariable @Min(value = 1,
                                      message = "userId must be greater than 0") int userId) {
        try {
            userService.deleteUser(userId);
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "user delete successfully");
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}" , e.getMessage() , e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "user delete fail");
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<UserResponse> getUser(@PathVariable @Min(1) int userId) {
        try {
            return new DataResponse<>(HttpStatus.OK.value(), "user", userService.getUserDetail(userId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<?> getAllUser(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam (defaultValue = "5")int pageSize,
            @RequestParam (required = false) String sortBy,
            @RequestParam(required = false) String keyword) {
        return new DataResponse<>(HttpStatus.OK.value(), "users", userService.getAllUser(pageNo, pageSize,sortBy,keyword));
    }

    @GetMapping("/view-profile")
    @PreAuthorize("hasAuthority('USER')")
    public DataResponse<UserResponse> viewProfile (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Bạn chưa đăng nhập!");
        }

        log.info("User từ SecurityContext: {}", authentication.getName());
        return new DataResponse<>(HttpStatus.OK.value(), "view info", userService.viewMyInfo());
    }

    @PatchMapping("/update-profile")
    public DataResponse<UserResponse> updateProfile (@RequestBody UserUpdateRequest request){
        return new DataResponse<>(HttpStatus.OK.value(), "update info", userService.updateProfile(request));
    }

    @GetMapping("/view-my-wish-list")
    @PreAuthorize("hasAuthority('USER')")
    public DataResponse<List<WishListResponse>> viewMyWishList (){
        return new DataResponse<>(HttpStatus.OK.value(), "view my wish list",userService.viewMyWishList());
    }

    @DeleteMapping("/delete-wish-list/{wishListId}")
    @PreAuthorize("hasAuthority('USER')")
    public DataResponse<?> deleteWishList(@PathVariable Long wishListId) {
        try {
            userService.deleteWishListByUser(wishListId);
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "user delete wish list successfully");
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}" , e.getMessage() , e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "user delete wish list fail");
        }
    }

}

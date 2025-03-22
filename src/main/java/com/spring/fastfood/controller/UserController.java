package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.response.ResponseData;
import com.spring.fastfood.dto.response.ResponseError;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
@Validated
public class UserController {


    @PostMapping("/")
    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequest request) {
        try {
            return new ResponseData(HttpStatus.CREATED.value(), "User added", 1);
        }catch (Exception e){
            return new ResponseError(HttpStatus.CREATED.value(), "Create user fail");
        }
    }

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) int userId, @Valid @RequestBody UserRequest user) {
        System.out.println("Update user");
        return new ResponseData(HttpStatus.ACCEPTED.value(), "User updated");
    }

    @PatchMapping("/{userId}")
    public ResponseData updateStatus(@Min(1) @PathVariable int userId,
                                        @RequestParam boolean status) {
        return new ResponseData(HttpStatus.ACCEPTED.value(), "User changed status");
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId) {
        return new ResponseData(HttpStatus.NO_CONTENT.value(), "User deleted");
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequest> getUser(@PathVariable @Min(1) int userId) {
        return new ResponseData(HttpStatus.OK.value(), "User",
                new UserRequest("Tay", "Java", "admin@tayjava.vn", "0123456789"));
    }

    @GetMapping("/list")
    public ResponseData<List<UserRequest>> getAllUser(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                      @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        return new ResponseData(HttpStatus.OK.value(), "User", List.of
                (new UserRequest("Tay", "Java", "admin@tayjava.vn", "0123456789"),
                 new UserRequest("Tay", "Java", "admin@tayjava.vn", "0123456789")));
    }
}

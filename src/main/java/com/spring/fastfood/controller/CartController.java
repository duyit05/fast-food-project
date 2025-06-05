package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.CartItemRequest;
import com.spring.fastfood.dto.response.CartResponse;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.service.CartService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/cart")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/view-all-cart")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<List<CartResponse>> getAllCart (){
        return new DataResponse<>(HttpStatus.OK.value(),"view all cart",cartService.getAllCart());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public DataResponse<CartResponse> addToCart (@RequestBody CartItemRequest request){
        return new DataResponse<>(HttpStatus.CREATED.value(),"create cart",cartService.addToCart(request));
    }

    @GetMapping("/my-cart")
    public DataResponse<CartResponse> getMyCart (){
        return new DataResponse<>(HttpStatus.OK.value(),"my cart",cartService.getMyCart());
    }

    @DeleteMapping("/delete-food-from-cart")
    public DataResponse<?> deleteFoodFromCart (@RequestParam long foodId) {
        try {
            cartService.deleteFromCart(foodId);
            return new DataResponse<>(HttpStatus.NO_CONTENT.value(),"delete food from cart success");
        }catch (ResourceNotFoundException e){
            log.error("error: {}", e.getMessage(),e.getCause());
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(),"delete food from cart fail");
        }
    }

    @DeleteMapping("/delete-cart/{cartId}")
    public DataResponse<?> deleteCart (@PathVariable long cartId) {
        try {
            cartService.deleteCart(cartId);
            return new DataResponse<>(HttpStatus.NO_CONTENT.value(),"delete cart success");
        }catch (ResourceNotFoundException e){
            log.error("error: {}", e.getMessage(),e.getCause());
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(),"delete cart fail");
        }
    }

    @GetMapping("/get-cart/{cartId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<CartResponse> getCartById (@PathVariable long cartId){
        return new DataResponse<>(HttpStatus.OK.value(),"get cart by id",cartService.getCartById(cartId));
    }
}

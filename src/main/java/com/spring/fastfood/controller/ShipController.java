package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.ShipRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.ShipResponse;
import com.spring.fastfood.service.ShipService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/ship")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ShipController {
    private final ShipService shipService;

    @GetMapping("/list")
    DataResponse<List<ShipResponse>> getAllShip (){
        return new DataResponse<>(HttpStatus.OK.value(), "get all ship",shipService.getAllShip());
    }
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    DataResponse<ShipResponse> createShip (@Valid @RequestBody ShipRequest request){
        return new DataResponse<>(HttpStatus.OK.value(), "create ship",shipService.createShip(request));
    }
    @PutMapping("/update/{shipId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    DataResponse<ShipResponse> createShip (@PathVariable long shipId,@Valid @RequestBody ShipRequest request){
        return new DataResponse<>(HttpStatus.OK.value(), "update ship",shipService.updateShipById(shipId,request));
    }
    @DeleteMapping("/delete/{shipId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    DataResponse<ShipResponse> createShip (@PathVariable long shipId){
        try {
            shipService.deleteShipById(shipId);
            return new DataResponse<>(HttpStatus.NO_CONTENT.value(), "delete ship success");
        }catch (RuntimeException e){
            log.info("error: {}", e.getMessage(),e.getCause());
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "delete ship fail");
        }
    }
}

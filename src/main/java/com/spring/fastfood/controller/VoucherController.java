package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.VoucherRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.VoucherResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/voucher")
@RestController
@RequiredArgsConstructor
@Slf4j
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    DataResponse<VoucherResponse> createVoucher (@Valid @RequestBody VoucherRequest request){
        return new DataResponse<>(HttpStatus.CREATED.value(),"create voucher",voucherService.createVoucher(request));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    DataResponse<List<VoucherResponse>> createVoucher (){
        return new DataResponse<>(HttpStatus.OK.value(),"get all voucher",voucherService.getAllVoucher());
    }

    @PutMapping("/update/{voucherId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    DataResponse<VoucherResponse> updateVoucher (@PathVariable long voucherId,@Valid @RequestBody VoucherRequest request){
        return new DataResponse<>(HttpStatus.ACCEPTED.value(), "update voucher", voucherService.updateVoucherById(voucherId,request));
    }

    @DeleteMapping("/delete/{voucherId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    DataResponse<?> deleteVoucher (@PathVariable long voucherId){
        try {
            voucherService.deleteVoucherById(voucherId);
            return new DataResponse<>(HttpStatus.NO_CONTENT.value(), "delete voucher success");
        }catch (ResourceNotFoundException e){
            log.info("error: {}", e.getMessage(),e.getCause());
            return new DataResponse<>(HttpStatus.NO_CONTENT.value(), "delete voucher fail");

        }
    }
}

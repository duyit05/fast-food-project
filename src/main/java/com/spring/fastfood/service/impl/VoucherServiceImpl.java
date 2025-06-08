package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.VoucherRequest;
import com.spring.fastfood.dto.response.VoucherResponse;
import com.spring.fastfood.enums.VoucherType;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.mapper.VoucherMapper;
import com.spring.fastfood.model.Voucher;
import com.spring.fastfood.repository.VoucherRepository;
import com.spring.fastfood.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    public String randomCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    @Override
    public VoucherResponse createVoucher(VoucherRequest request) {
        Voucher voucher = Voucher.builder()
                .code(randomCode())
                .description(request.getDescription())
                .discount(request.getDiscount())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .status(VoucherType.VALID)
                .build();
        return voucherMapper.toVoucherResponse(voucherRepository.save(voucher));
    }

    @Override
    public List<VoucherResponse> getAllVoucher() {
        List<Voucher> vouchers = voucherRepository.findAll();
        return vouchers.stream().map(voucherMapper::toVoucherResponse).collect(Collectors.toList());
    }

    @Override
    public VoucherResponse updateVoucherById(long voucherId, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found voucher with id: " + voucherId));
        voucherMapper.updateVoucher(voucher, request);
        return voucherMapper.toVoucherResponse(voucherRepository.save(voucher));
    }

    @Override
    public void deleteVoucherById(long voucherId) {
        voucherRepository.deleteById(voucherId);
        log.info("delete voucher success");
    }
}

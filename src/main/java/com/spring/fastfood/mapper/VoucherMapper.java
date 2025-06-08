package com.spring.fastfood.mapper;

import com.spring.fastfood.dto.request.VoucherRequest;
import com.spring.fastfood.dto.response.VoucherResponse;
import com.spring.fastfood.model.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    @Mapping(source = "id", target = "voucherId")
    VoucherResponse toVoucherResponse (Voucher voucher);
    void updateVoucher(@MappingTarget Voucher voucher, VoucherRequest request);
}

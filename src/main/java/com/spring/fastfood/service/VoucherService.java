package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.CheckVoucherRequest;
import com.spring.fastfood.dto.request.VoucherRequest;
import com.spring.fastfood.dto.response.CheckVoucherResponse;
import com.spring.fastfood.dto.response.SuccessResponse;
import com.spring.fastfood.dto.response.VoucherResponse;
import java.util.List;

public interface VoucherService {

    VoucherResponse createVoucher (VoucherRequest request);
    List<VoucherResponse> getAllVoucher ();
    VoucherResponse updateVoucherById (long voucherId, VoucherRequest request);
    void deleteVoucherById (long voucherId);

    void checkVoucher(long voucherId, CheckVoucherRequest request);
}

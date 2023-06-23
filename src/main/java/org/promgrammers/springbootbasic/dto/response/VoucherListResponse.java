package org.promgrammers.springbootbasic.dto.response;

import java.util.List;

public record VoucherListResponse(List<VoucherResponse> voucherResponseList) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("유효한 바우처 목록 \n");
        for (VoucherResponse voucherResponse : voucherResponseList) {
            sb.append("  ").append(voucherResponse.toString()).append("\n");
        }
        return sb.toString();
    }
}

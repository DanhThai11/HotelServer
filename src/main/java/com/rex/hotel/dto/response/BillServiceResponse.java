package com.rex.hotel.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillServiceResponse {
    private Long id;
    private String serviceName; // 🔥 Thêm tên dịch vụ
    private Integer quantity;
    private BigDecimal price;
}


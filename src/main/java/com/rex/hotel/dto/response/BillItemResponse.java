package com.rex.hotel.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillItemResponse {
    private Long id;
    private String itemName; // 🔥 Thêm tên mặt hàng
    private Integer quantity;
    private BigDecimal price;
}


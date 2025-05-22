package com.rex.hotel.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillServiceRequest {
    Long serviceId; // ID của dịch vụ được sử dụng
    Integer quantity;
    BigDecimal price;
}

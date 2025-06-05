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
public class BillItemRequest {
     Long inventoryItemId; // ID của mặt hàng trong kho
     Integer quantity;
     BigDecimal price;
}

package com.rex.hotel.dto.response;

import com.rex.hotel.enums.ItemCategory;
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
public class InventoryItemResponse {
     Long id;
     String name;
     BigDecimal price;
     ItemCategory category;
}

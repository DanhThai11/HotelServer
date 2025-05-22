package com.rex.hotel.dto.request;

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
public class ItemRequest {
      String name;
      String code;
      ItemCategory category;
      Integer quantity;
      String unit;
      Integer minQuantity;
      BigDecimal price;
      String supplier;
      String description;
}

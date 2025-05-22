package com.rex.hotel.dto.response;

import com.rex.hotel.enums.ServiceCategory;
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
public class ServiceResponse {
     Long id;
     String name;
     String description;
     BigDecimal price;
     ServiceCategory category;
}

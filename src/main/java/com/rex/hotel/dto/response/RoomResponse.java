package com.rex.hotel.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
     Long id;
     String name;
     String description;
     BigDecimal price;
     String type;
     int capacity;
     String status;
     String photo;
}

package com.rex.hotel.dto.request;

import com.rex.hotel.enums.RoomStatus;
import com.rex.hotel.enums.RoomType;
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
public class RoomRequest {
    String name;
    String roomNumber;
    String description;
    BigDecimal price;
    RoomType type;
    int capacity;
    RoomStatus status;
}

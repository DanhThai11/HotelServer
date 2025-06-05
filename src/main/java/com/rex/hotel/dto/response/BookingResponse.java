package com.rex.hotel.dto.response;


import com.rex.hotel.enums.BookingStatus;
import com.rex.hotel.enums.RoomType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
     Long id;
     UserSummary user;
     Long roomId;
     String roomNumber;
     RoomType roomType;
     LocalDateTime checkInDate;
     LocalDateTime checkOutDate;
     int numberOfGuests;
     BigDecimal totalAmount;
     String specialRequests;
     BookingStatus status;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
     Long billId;

    @Getter
    @Setter
    @Builder
    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserSummary {
        private String id;
        private String username;
    }
}

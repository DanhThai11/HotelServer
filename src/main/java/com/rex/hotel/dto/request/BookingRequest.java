package com.rex.hotel.dto.request;


import com.rex.hotel.enums.BookingStatus;
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
public class BookingRequest {
     Long roomId;
     LocalDateTime checkInDate;
     LocalDateTime checkOutDate;
     int numberOfGuests;
     String specialRequests;
}

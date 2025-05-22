package com.rex.hotel.dto.response;

import com.rex.hotel.enums.BillStatus;
import com.rex.hotel.enums.RoomType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillResponse {
     private Long id;
     private Long bookingId;
     private BigDecimal roomCharge;
     private RoomType roomType;
     private List<BillItemResponse> items;
     private List<BillServiceResponse> services;
     private BigDecimal totalAmount;
     private BillStatus status;
     private LocalDateTime createdAt;
     private LocalDateTime paidAt;
}


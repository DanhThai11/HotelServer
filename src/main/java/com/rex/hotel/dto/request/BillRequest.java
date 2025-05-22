package com.rex.hotel.dto.request;

import com.rex.hotel.enums.BillStatus;
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
public class BillRequest {
    Long bookingId;
    BigDecimal roomCharge;
    List<BillItemRequest> items;
    List<BillServiceRequest> services;
}

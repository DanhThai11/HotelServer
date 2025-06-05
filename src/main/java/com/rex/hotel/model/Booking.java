package com.rex.hotel.model;

import com.rex.hotel.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Người dùng không được để trống")
     User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @NotNull(message = "Phòng không được để trống")
     Room room;

    @NotNull(message = "Ngày nhận phòng không được để trống")
     LocalDateTime checkInDate;

    @NotNull(message = "Ngày trả phòng không được để trống")
     LocalDateTime checkOutDate;

    @Min(value = 1, message = "Số lượng khách phải lớn hơn 0")
     int numberOfGuests;

    @Column(nullable = false)
     BigDecimal totalAmount;

    @Column(length = 500)
     String specialRequests;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Trạng thái không được để trống")
     BookingStatus status = BookingStatus.PENDING;

    @Column(updatable = false)
     LocalDateTime createdAt;

     LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = BookingStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 
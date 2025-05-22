package com.rex.hotel.model;

import com.rex.hotel.enums.BillStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
     Booking booking;

    @Column(nullable = false)
     BigDecimal roomCharge;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
     List<BillItem> items;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
     List<BillService> services;

    @Column(nullable = false)
     BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
     BillStatus status;

    @Column(nullable = false)
     LocalDateTime createdAt;

    @Column
    private LocalDateTime paidAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

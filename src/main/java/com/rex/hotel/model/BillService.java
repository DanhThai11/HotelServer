package com.rex.hotel.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bill_services")
public class BillService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
     Bill bill;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    ServiceHotel service;

    @Column(nullable = false)
     Integer quantity;

    @Column(nullable = false)
     BigDecimal price;
} 
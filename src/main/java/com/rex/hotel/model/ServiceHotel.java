package com.rex.hotel.model;

import com.rex.hotel.enums.ServiceCategory;
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
@Table(name = "services")
public class ServiceHotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @Column(nullable = false)
     String name;

    @Enumerated(EnumType.STRING)
     ServiceCategory category;

    @Column(nullable = false)
     BigDecimal price;

    @Column
     String description;

    @Column(name = "is_available", nullable = false)
     Boolean isAvailable = true;
}


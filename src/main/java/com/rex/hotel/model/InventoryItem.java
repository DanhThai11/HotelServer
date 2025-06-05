package com.rex.hotel.model;

import com.rex.hotel.enums.ItemCategory;
import jakarta.persistence.*;
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
@Table(name = "inventory_items")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @Column(nullable = false)
     String name;

    @Column(nullable = false)
     String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
     ItemCategory category;

    @Column(nullable = false)
     Integer quantity;

    @Column(nullable = false)
     String unit;

    @Column(nullable = false)
     Integer minQuantity;

    @Column(nullable = false)
     BigDecimal price;

    @Column
     String supplier;

    @Column
     String description;

    @Column(nullable = false)
     LocalDateTime createdAt;

    @Column
     LocalDateTime updatedAt;

    @Column
     LocalDateTime lastRestockAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

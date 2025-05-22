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
@Table(name = "bill_items")
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
     Bill bill;

    @ManyToOne
    @JoinColumn(name = "inventory_item_id", nullable = false)
     InventoryItem item;

    @Column(nullable = false)
     Integer quantity;

    @Column(nullable = false)
     BigDecimal price;
} 
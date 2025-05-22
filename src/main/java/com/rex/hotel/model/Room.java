package com.rex.hotel.model;

import com.rex.hotel.enums.RoomStatus;
import com.rex.hotel.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @Column(nullable = false, unique = true)
     String roomNumber;

    @Enumerated(EnumType.STRING)
     RoomType type;

    @Column(nullable = false)
     BigDecimal price;

    @Enumerated(EnumType.STRING)
     RoomStatus status;

    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
     List<String> amenities;

    @Column
     Integer capacity;

    @Column
     String description;

    @Column(name = "image_url")
     String imagePath;

}



package com.rex.hotel.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

     LocalDateTime expiryDate;
}


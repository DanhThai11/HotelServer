package com.rex.hotel.model;

import com.rex.hotel.enums.AccountStatus;
import com.rex.hotel.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     String id;
     String username;
     String password;
     String fullName;
     String email;
     String phoneNumber;
     Set<String> roles;
     @Enumerated(EnumType.STRING)
     AccountStatus accountStatus;
     @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     EmailVerificationToken emailVerificationToken;
}


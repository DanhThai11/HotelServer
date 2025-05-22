package com.rex.hotel.service;

import com.rex.hotel.dto.request.RegisterRequest;
import com.rex.hotel.dto.request.UserUpdateRequest;
import com.rex.hotel.dto.response.UserResponse;
import com.rex.hotel.enums.AccountStatus;
import com.rex.hotel.enums.Role;
import com.rex.hotel.exception.AppException;
import com.rex.hotel.exception.ErrorCode;
import com.rex.hotel.mapper.UserMapper;
import com.rex.hotel.model.EmailVerificationToken;
import com.rex.hotel.model.User;
import com.rex.hotel.repository.BookingRepository;
import com.rex.hotel.repository.EmailVerificationTokenRepository;
import com.rex.hotel.repository.UserRepository;
import com.rex.hotel.util.SecurityUtils;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    BookingRepository bookingRepository;
    EmailVerificationTokenRepository tokenRepository;
    EmailService emailService;
    SecurityUtils securityUtils;

    @Transactional
    public void register(RegisterRequest request) throws MessagingException {
        // 1. Tạo user INACTIVE
        User user = new User();
        Set<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAccountStatus(AccountStatus.INACTIVE);
        user.setRoles(roles);
        userRepository.save(user);

        // 2. Tạo token
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        tokenRepository.save(verificationToken);

        // 3. Gửi email
        emailService.sendVerificationEmail(user.getEmail(), token);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getInfo(){
        String name = securityUtils.getCurrentUsername();
        System.out.println(name);
        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse findById(String id){
        return userMapper.toUserResponse(userRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Bố đéo tìm thấy")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateById(String id, UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Bố đéo tim thay"));
        userMapper.toUpdateUser(user , request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(String id){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!bookingRepository.findByUser(user).isEmpty()) {
            throw new AppException(ErrorCode.USER_HAS_BOOKINGS);
        }
        userRepository.deleteById(user.getId());

        userRepository.deleteById(user.getId());
        return "Xoa thanh cong dit con me m";
    }



}

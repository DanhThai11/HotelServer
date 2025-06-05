package com.rex.hotel.controller;

import com.nimbusds.jose.JOSEException;
import com.rex.hotel.dto.request.*;
import com.rex.hotel.dto.response.ApiResponse;
import com.rex.hotel.dto.response.AuthenticationResponse;
import com.rex.hotel.dto.response.IntrospectResponse;
import com.rex.hotel.exception.AppException;
import com.rex.hotel.exception.ErrorCode;
import com.rex.hotel.model.User;
import com.rex.hotel.repository.UserRepository;
import com.rex.hotel.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;


    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        var result = authService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/changePass")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request) throws AppException {
        authService.changePassword(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Mật khẩu đã được đổi thành công")
                .build();
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        authService.confirmEmail(token);
        return ResponseEntity.ok("Xác thực tài khoản thành công! Bạn có thể đăng nhập.");
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefeshTokenRequest request)
            throws ParseException, JOSEException {
        var result = authService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authService.introspectToken(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

} 
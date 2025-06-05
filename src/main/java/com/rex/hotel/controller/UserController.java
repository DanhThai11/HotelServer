package com.rex.hotel.controller;

import com.rex.hotel.dto.request.RegisterRequest;
import com.rex.hotel.dto.request.UserUpdateRequest;
import com.rex.hotel.dto.response.ApiResponse;
import com.rex.hotel.dto.response.UserResponse;
import com.rex.hotel.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
     ApiResponse<Void> addUser(@RequestBody RegisterRequest registerRequest) throws MessagingException {
        userService.register(registerRequest);
        return ApiResponse.<Void>builder()
                .message("Kiem tra email")
                .build();

    }

     @GetMapping
     ApiResponse<List<UserResponse>> getAll(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .message("Lấy dữ liệu thành công")
                .build();
    }

    @GetMapping("/{id}")
     ApiResponse<UserResponse> findById(@PathVariable String id){
        return ApiResponse.<UserResponse>builder()
                .result(userService.findById(id))
                .message("Tìm thành công")
                .build();
    }

    @PutMapping
     ApiResponse<UserResponse> updateById(@PathVariable String id, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateById(id, request))
                .message("Cập nhật thành công")
                .build();
    }

    @GetMapping("myInfo")
     ApiResponse<UserResponse> myInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getInfo())
                .message("Thông tin cá nhân")
                .build();
    }

    @DeleteMapping("/{id}")
     ApiResponse<Void> deleteById(@PathVariable String id){
        userService.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xoá thành công")
                .build();
    }

}

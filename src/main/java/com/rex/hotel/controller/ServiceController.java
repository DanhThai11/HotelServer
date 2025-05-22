package com.rex.hotel.controller;

import com.rex.hotel.dto.request.ServiceRequest;
import com.rex.hotel.dto.response.ApiResponse;
import com.rex.hotel.dto.response.ServiceResponse;
import com.rex.hotel.model.ServiceHotel;
import com.rex.hotel.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServiceController {
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAllServices() {
        return ResponseEntity.ok(ApiResponse.<List<ServiceResponse>>builder()
                .message("Lấy danh sách dịch vụ thành công")
                .result(hotelService.getAllServices())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ServiceResponse>builder()
                .message("Lấy thông tin dịch vụ thành công")
                .result(hotelService.getServiceById(id))
                .build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getServicesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(ApiResponse.<List<ServiceResponse>>builder()
                .message("Lấy danh sách dịch vụ theo loại thành công")
                .result(hotelService.getServicesByCategory(category))
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(@Valid @RequestBody ServiceRequest serviceHotel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ServiceResponse>builder()
                        .message("Dịch vụ đã được tạo thành công")
                        .result(hotelService.createService(serviceHotel))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(@PathVariable Long id, @Valid @RequestBody ServiceRequest serviceHotel) {
        return ResponseEntity.ok(ApiResponse.<ServiceResponse>builder()
                .message("Dịch vụ đã được cập nhật thành công")
                .result(hotelService.updateService(id, serviceHotel))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        hotelService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Dịch vụ đã được xóa thành công")
                .result(null)
                .build());
    }
} 
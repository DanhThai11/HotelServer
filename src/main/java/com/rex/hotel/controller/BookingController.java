package com.rex.hotel.controller;

import com.rex.hotel.dto.request.BookingRequest;
import com.rex.hotel.dto.response.ApiResponse;
import com.rex.hotel.dto.response.BookingResponse;
import com.rex.hotel.model.Booking;
import com.rex.hotel.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        return ResponseEntity.ok(
                ApiResponse.<List<BookingResponse>>builder()
                        .message("Lấy danh sách đặt phòng thành công")
                        .result(bookingService.getAllBookings())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<BookingResponse>builder()
                .message("Lấy thông tin đặt phòng thành công")
                .result(bookingService.getBookingById(id))
                .build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                .message("Lấy danh sách đặt phòng của người dùng thành công")
                .result(bookingService.getBookingsByUser(userId))
                .build());
    }

    @GetMapping("/user/my")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByCurrentUser() {
        List<BookingResponse> bookings = bookingService.getBookingByUserIdForUser();
        return ResponseEntity.ok(
                ApiResponse.<List<BookingResponse>>builder()
                        .code(0)
                        .message("Lấy danh sách đặt phòng theo người dùng thành công")
                        .result(bookings)
                        .build()
        );
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                .message("Lấy danh sách đặt phòng của phòng thành công")
                .result(bookingService.getBookingsByRoom(roomId))
                .build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                .message("Lấy danh sách đặt phòng theo trạng thái thành công")
                .result(bookingService.getBookingsByStatus(status))
                .build());
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                .message("Lấy danh sách đặt phòng trong khoảng thời gian thành công")
                .result(bookingService.getBookingsBetweenDates(startDate, endDate))
                .build());
    }


    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest booking) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BookingResponse>builder()
                        .message("Đặt phòng thành công")
                        .result(bookingService.createBooking(booking))
                        .build()
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequest booking) {
        return ResponseEntity.ok(ApiResponse.<BookingResponse>builder()
                .message("Cập nhật đặt phòng thành công")
                .result(bookingService.updateBooking(id, booking))
                .build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.<BookingResponse>builder()
                .message("Cập nhật trạng thái đặt phòng thành công")
                .result(bookingService.updateBookingStatus(id, status))
                .build());
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.<BookingResponse>builder()
                        .message("Hủy đặt phòng thành công")
                        .result(bookingService.cancelBooking(id))
                        .build());
    }


    @GetMapping("/check-in")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getTodayCheckIns() {
        return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                .message("Lấy danh sách check-in hôm nay thành công")
                .result(bookingService.getTodayCheckIns())
                .build());
    }

    @GetMapping("/check-out")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getTodayCheckOuts() {
        return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                .message("Lấy danh sách check-out hôm nay thành công")
                .result(bookingService.getTodayCheckOuts())
                .build());
    }
} 
package com.rex.hotel.controller;

import com.rex.hotel.dto.request.BillItemRequest;
import com.rex.hotel.dto.request.BillRequest;
import com.rex.hotel.dto.request.BillServiceRequest;
import com.rex.hotel.dto.response.ApiResponse;
import com.rex.hotel.dto.response.BillResponse;
import com.rex.hotel.model.*;
import com.rex.hotel.service.BillManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BillController {
    private final BillManagementService billService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BillResponse>>> getAllBills() {
        return ResponseEntity.ok(ApiResponse.<List<BillResponse>>builder()
                .message("Lấy danh sách hóa đơn thành công")
                .result(billService.getAllBills())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponse>> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
                .message("Lấy thông tin hóa đơn thành công")
                .result(billService.getBillById(id))
                .build());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<BillResponse>> getBillByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
                .message("Lấy hóa đơn theo mã đặt phòng thành công")
                .result(billService.getBillByBookingId(bookingId))
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BillResponse>> createBill(@Valid @RequestBody BillRequest bill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BillResponse>builder()
                        .message("Hóa đơn đã được tạo thành công")
                        .result(billService.createBill(bill))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponse>> updateBill(@PathVariable Long id, @Valid @RequestBody BillRequest bill) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
                .message("Hóa đơn đã được cập nhật thành công")
                .result(billService.updateBill(id, bill))
                .build());
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<ApiResponse<BillResponse>> addBillItem(@PathVariable Long id, @Valid @RequestBody BillItemRequest item) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
                .message("Đã thêm mặt hàng vào hóa đơn")
                .result(billService.addBillItem(id, item))
                .build());
    }

    @PostMapping("/{id}/services")
    public ResponseEntity<ApiResponse<BillResponse>> addBillService(@PathVariable Long id, @Valid @RequestBody BillServiceRequest service) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
                .message("Đã thêm dịch vụ vào hóa đơn")
                .result(billService.addBillService(id, service))
                .build());
    }

    @DeleteMapping("/{billId}/items/{itemId}")
    public ResponseEntity<ApiResponse<BillResponse>> removeBillItem(@PathVariable Long billId, @PathVariable Long itemId) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
                .message("Đã xóa mặt hàng khỏi hóa đơn")
                .result(billService.removeBillItem(billId, itemId))
                .build());
    }

    @DeleteMapping("/{billId}/services/{serviceId}")
    public ResponseEntity<ApiResponse<BillResponse>> removeBillService(@PathVariable Long billId, @PathVariable Long serviceId) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
                .message("Đã xóa dịch vụ khỏi hóa đơn")
                .result(billService.removeBillService(billId, serviceId))
                .build());
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<BillResponse>> payBill(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<BillResponse>builder()
                .message("Hóa đơn đã được thanh toán thành công")
                .result(billService.payBill(id))
                .build());
    }

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<Double>> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(ApiResponse.<Double>builder()
                .message("Tính doanh thu thành công")
                .result(billService.calculateRevenue(startDate, endDate))
                .build());
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<BillResponse>>> getOverdueBills() {
        return ResponseEntity.ok(ApiResponse.<List<BillResponse>>builder()
                .message("Lấy danh sách hóa đơn quá hạn thành công")
                .result(billService.getOverdueBills())
                .build());
    }
} 
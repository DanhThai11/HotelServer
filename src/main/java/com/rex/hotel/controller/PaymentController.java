package com.rex.hotel.controller;

import com.rex.hotel.dto.response.ApiResponse;
import com.rex.hotel.service.PaymentService;
import com.rex.hotel.service.BillManagementService;
import com.rex.hotel.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final BillManagementService billManagementService;

    @GetMapping("/vnpay/create")
    public ResponseEntity<ApiResponse<String>> createPayment(
            @RequestParam Long billId,
            @RequestParam Long amount,
            HttpServletRequest request) {
        String paymentUrl = paymentService.createPaymentUrl(billId, amount, request);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Tạo URL thanh toán thành công")
                .result(paymentUrl)
                .build());
    }


    @GetMapping("/vnpay/return")
    public void paymentReturn(
            @RequestParam Map<String, String> vnpParams,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // 1. Xác thực chữ ký từ VNPAY
        String secureHash = vnpParams.get("vnp_SecureHash");
        String txnRef = vnpParams.get("vnp_TxnRef");
        String responseCode = vnpParams.get("vnp_ResponseCode");

        String redirectUrl = "http://localhost:5173/booking-success";

        if (secureHash == null) {
            // Xử lý lỗi thiếu chữ ký và chuyển hướng về frontend với thông báo lỗi
             String finalRedirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                    .queryParam("paymentStatus", "failed")
                    .queryParam("message", "Thiếu chữ ký bảo mật")
                    .build().toUriString();
             response.sendRedirect(finalRedirectUrl);
             return;
        }

        boolean isValidSignature = paymentService.verifyVNPaySignature(vnpParams, secureHash);

        if (!isValidSignature) {
            // Xử lý lỗi chữ ký không hợp lệ và chuyển hướng về frontend với thông báo lỗi
             String finalRedirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                    .queryParam("paymentStatus", "failed")
                    .queryParam("message", "Chữ ký không hợp lệ")
                    .build().toUriString();
             response.sendRedirect(finalRedirectUrl);
             return;
        }

        // 2. Kiểm tra Response Code và cập nhật trạng thái hóa đơn

        if ("00".equals(responseCode)) {
            // Thanh toán thành công
            Long billId = Long.valueOf(txnRef);
            billManagementService.payBill(billId); // Gọi hàm cập nhật trạng thái PAID cho Bill

            String finalRedirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                    .queryParam("paymentStatus", "success")
                    .queryParam("billId", txnRef)
                    .build().toUriString();
             response.sendRedirect(finalRedirectUrl);

        } else {
            // Thanh toán thất bại hoặc các trạng thái khác
             String finalRedirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                    .queryParam("paymentStatus", "failed")
                    .queryParam("billId", txnRef)
                    .build().toUriString();
             response.sendRedirect(finalRedirectUrl);
        }
    }
} 
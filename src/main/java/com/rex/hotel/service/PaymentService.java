package com.rex.hotel.service;

import com.rex.hotel.config.VNPayConfig;
import com.rex.hotel.exception.AppException;
import com.rex.hotel.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Hex;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;

    private static final boolean DEBUG_FIXED_IP = false; // Set to true to use a fixed IP for debugging
    private static final String FIXED_DEBUG_IP = "127.0.0.1"; // Your fixed IP for debugging

    public String createPaymentUrl(Long billId, Long amount, HttpServletRequest request) {
        String vnp_TmnCode = vnPayConfig.getVnpTmnCode();
        String vnp_HashSecret = vnPayConfig.getVnpHashSecret();
        String vnp_Url = vnPayConfig.getVnpUrl();
        String vnp_ReturnUrl = vnPayConfig.getVnpReturnUrl();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", String.valueOf(billId));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don " + billId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", getIpAddress(request));
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        try {
            for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    String encodedFieldName = URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString());
                    String encodedFieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());

                    // Build hash data
                    hashData.append(encodedFieldName);
                    hashData.append('=');
                    hashData.append(encodedFieldValue);

                    // Build query url
                    query.append(encodedFieldName);
                    query.append('=');
                    query.append(encodedFieldValue);

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return vnp_Url + "?" + queryUrl;
    }

    private String getIpAddress(HttpServletRequest request) {
        if (DEBUG_FIXED_IP) {
            return FIXED_DEBUG_IP; // Return fixed IP for debugging
        }
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private String hmacSHA512(String key, String data) {
        try {
            javax.crypto.Mac hmac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hmacBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hmacBytes); // HEX encode here
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public boolean verifyVNPaySignature(Map<String, String> vnpParams, String secureHash) {
        String vnp_HashSecret = vnPayConfig.getVnpHashSecret();
        Map<String, String> params = new HashMap<>(vnpParams);
        params.remove("vnp_SecureHash"); // Loại bỏ chữ ký VNPAY gửi về trước khi tính hash

        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();

        try {
            for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Mã hóa URL cả tên và giá trị tham số trước khi nối chuỗi hash
                    String encodedFieldName = URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString());
                    String encodedFieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());

                    hashData.append(encodedFieldName);
                    hashData.append('=');
                    hashData.append(encodedFieldValue);

                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
             throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        String mySecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());

        // So sánh chữ ký của bạn với chữ ký VNPAY gửi về (không phân biệt chữ hoa/thường)
        return mySecureHash.equalsIgnoreCase(secureHash);
    }
} 
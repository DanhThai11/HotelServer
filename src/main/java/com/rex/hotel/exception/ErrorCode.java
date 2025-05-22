package com.rex.hotel.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Exception", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "User Existed",HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1002, "Key Wrong",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004,"Password must be at least 8 characters",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not Existed",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006,"Account isn't authenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"You do not have permission",HttpStatus.FORBIDDEN),
    ROOM_EXISTED(1008,"Room Existed", HttpStatus.BAD_REQUEST),
    ROOM_NOT_EXISTED(1009,"RoomID not Existed", HttpStatus.BAD_REQUEST),
    BILL_NOT_EXISTED(1010,"BillID not Existed", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_EXISTED(1011,"BookingID not Existed", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_EXISTED(1012,"InventoryID not Existed", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_STOCK(1013,"Not enough inventory", HttpStatus.BAD_REQUEST),
    SERVICE_NOT_EXISTED(1014,"ServiceID not Existed", HttpStatus.BAD_REQUEST),
    BILL_ITEM_NOT_EXISTED(1015,"BillItemID not Existed", HttpStatus.BAD_REQUEST),
    BILL_ALREADY_PAID(1016,"Bill has been paid", HttpStatus.BAD_REQUEST),
    BILL_SERVICE_NOT_EXISTED(1017,"BillServiceID not Existed", HttpStatus.BAD_REQUEST),
    USER_HAS_BOOKINGS(1018,"User has booked", HttpStatus.BAD_REQUEST),
    INVALID_BOOKING_DATE(1019,"Lồn book phòng 0 ngày là con cặc gì, t đập m bây giờ", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1020,"Token invalid", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1021,"Token expired", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_VERIFIED(1022,"Email verification token not verified", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_FOUND(1023,"Booking not found", HttpStatus.BAD_REQUEST),
    ITEM_NOT_FOUND(1024,"Item not found", HttpStatus.BAD_REQUEST),
    SERVICE_NOT_FOUND(1025,"Service not found", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private int code;
    private String message;
    private HttpStatus status;

}

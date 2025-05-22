package com.rex.hotel.controller;

import com.rex.hotel.dto.request.ItemRequest;
import com.rex.hotel.dto.request.RoomRequest;
import com.rex.hotel.dto.request.ServiceRequest;
import com.rex.hotel.dto.response.ApiResponse;
import com.rex.hotel.dto.response.InventoryItemResponse;
import com.rex.hotel.dto.response.RoomResponse;
import com.rex.hotel.dto.response.ServiceResponse;
import com.rex.hotel.service.InventoryItemService;
import com.rex.hotel.service.RoomService;
import com.rex.hotel.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {
    private final RoomService roomService;
    private final InventoryItemService itemService;
    private final ServiceService serviceService;



    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getAllRooms() {
        return ResponseEntity.ok(ApiResponse.<List<RoomResponse>>builder()
                .message("Lấy danh sách phòng thành công")
                .result(roomService.getAllRooms())
                .build());
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<String>>> getAllRoomTypes() {
        return ResponseEntity.ok(ApiResponse.<List<String>>builder()
                .message("Lấy danh sách kiểu phòng thành công")
                .result(roomService.getRoomType())
                .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<RoomResponse>builder()
                .message("Lấy thông tin phòng thành công")
                .result(roomService.getRoomById(id))
                .build());
    }

    @PostMapping("/{roomId}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> uploadRoomImage(
            @PathVariable Long roomId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(roomService.uploadRoomImage(roomId, file));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getAvailableRooms(
            @RequestParam String checkIn,
            @RequestParam String checkOut) {
        return ResponseEntity.ok(ApiResponse.<List<RoomResponse>>builder()
                .message("Lấy danh sách phòng trống thành công")
                .result(roomService.getAvailableRooms(checkIn, checkOut))
                .build());
    }



    @PostMapping
    public ResponseEntity<ApiResponse<RoomResponse>> createRoom(@Valid @RequestBody RoomRequest room) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<RoomResponse>builder()
                        .message("Phòng đã được tạo thành công")
                        .result(roomService.createRoom(room))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest room) {
        return ResponseEntity.ok(ApiResponse.<RoomResponse>builder()
                .message("Phòng đã được cập nhật thành công")
                .result(roomService.updateRoom(id, room))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Phòng đã được xóa thành công")
                .result(null)
                .build());
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> createItem(@RequestBody ItemRequest request) {
        InventoryItemResponse itemResponse = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<InventoryItemResponse>builder()
                        .message("Tạo item thành công")
                        .result(itemResponse)
                        .build()
        );
    }

    @PostMapping("/services")
    public ResponseEntity<ApiResponse<ServiceResponse>> createItem(@RequestBody ServiceRequest request) {
        ServiceResponse serviceResponse = serviceService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ServiceResponse>builder()
                        .message("Tạo dịch vụ thành công")
                        .result(serviceResponse)
                        .build()
        );
    }
} 
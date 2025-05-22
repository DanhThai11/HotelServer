package com.rex.hotel.service;

import com.rex.hotel.dto.request.RoomRequest;
import com.rex.hotel.dto.response.RoomResponse;
import com.rex.hotel.enums.RoomStatus;
import com.rex.hotel.enums.RoomType;
import com.rex.hotel.exception.AppException;
import com.rex.hotel.exception.ErrorCode;
import com.rex.hotel.mapper.RoomMapper;
import com.rex.hotel.model.Room;
import com.rex.hotel.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoomService {
    RoomRepository roomRepository;
    RoomMapper roomMapper;

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toRoomResponse)
                .toList();
    }

    public List<String> getRoomType(){
        return Arrays.stream(RoomType.values())
                .map(Enum::name)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        return roomMapper.toRoomResponse(room);
    }

    public List<RoomResponse> getAvailableRooms(String checkIn, String checkOut) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime checkInDate = LocalDateTime.parse(checkIn, formatter);
        LocalDateTime checkOutDate = LocalDateTime.parse(checkOut, formatter);

        return roomRepository.findAvailableRooms(checkInDate, checkOutDate)
                .stream()
                .map(roomMapper::toRoomResponse)
                .toList();
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new AppException(ErrorCode.ROOM_EXISTED);
        }

        Room room = roomMapper.toRoom(request);
        room.setStatus(RoomStatus.AVAILABLE); // Set mặc định là AVAILABLE
        return roomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        // Cập nhật thông tin
        existingRoom.setRoomNumber(request.getRoomNumber());
        existingRoom.setType(request.getType());
        existingRoom.setPrice(request.getPrice());
        existingRoom.setCapacity(request.getCapacity());
        existingRoom.setDescription(request.getDescription());
        existingRoom.setStatus(request.getStatus());

        return roomMapper.toRoomResponse(roomRepository.save(existingRoom));
    }

    @Transactional
    public void deleteRoom(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        roomRepository.deleteById(room.get().getId());
    }

    @Transactional
    public RoomResponse uploadRoomImage(Long roomId, MultipartFile file) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        try {
            String uploadDir = "uploads/rooms/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            room.setImagePath("/uploads/rooms/" + fileName);
            return roomMapper.toRoomResponse(roomRepository.save(room));

        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu ảnh: " + e.getMessage());
        }
    }
}
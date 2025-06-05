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
import jakarta.persistence.EntityManager;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.engine.jdbc.LobCreationContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        return roomMapper.toRoomResponse(room);
    }

    public List<RoomResponse> getAvailableRooms(String checkIn, String checkOut, String roomType) {
        LocalDateTime checkInDate;
        LocalDateTime checkOutDate;
        
        try {
            // Thử parse với định dạng ISO_DATE_TIME (yyyy-MM-dd'T'HH:mm:ss)
            DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
            checkInDate = LocalDateTime.parse(checkIn, isoFormatter);
            checkOutDate = LocalDateTime.parse(checkOut, isoFormatter);
            System.out.println("Parse thành công với ISO_DATE_TIME: " + checkInDate + " - " + checkOutDate);
        } catch (DateTimeParseException e) {
            try {
                // Nếu không được, thử parse với định dạng ISO_DATE (yyyy-MM-dd)
                DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
                checkInDate = LocalDate.parse(checkIn, dateFormatter).atStartOfDay();
                checkOutDate = LocalDate.parse(checkOut, dateFormatter).atTime(23, 59, 59);
                System.out.println("Parse thành công với ISO_DATE: " + checkInDate + " - " + checkOutDate);
            } catch (DateTimeParseException ex) {
                System.out.println("Lỗi parse ngày tháng: " + ex.getMessage());
                throw new AppException(ErrorCode.INVALID_BOOKING_DATE);
            }
        }
        
        RoomType type = null;
        if (roomType != null && !roomType.isEmpty()) {
            try {
                type = RoomType.valueOf(roomType.toUpperCase());
                System.out.println("Loại phòng: " + type);
            } catch (IllegalArgumentException e) {
                System.out.println("Lỗi loại phòng không hợp lệ: " + roomType);
                throw new AppException(ErrorCode.INVALID_ROOM_TYPE);
            }
        }

        List<Room> availableRooms;
        if (type != null) {
            availableRooms = roomRepository.findAvailableRoomsByType(checkInDate, checkOutDate, type);
        } else {
            availableRooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate);
        }
        
        System.out.println("Số phòng khả dụng tìm được: " + availableRooms.size());
        if (availableRooms.isEmpty()) {
            System.out.println("Không tìm thấy phòng nào khả dụng trong khoảng thời gian: " + checkInDate + " - " + checkOutDate);
        }

        return availableRooms.stream()
                .map(roomMapper::toRoomResponse)
                .toList();
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new AppException(ErrorCode.ROOM_EXISTED);
        }

        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.INVALID_PRICE);
        }

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setType(request.getType());
        room.setPrice(request.getPrice());
        room.setCapacity(request.getCapacity());
        room.setDescription(request.getDescription());
        room.setStatus(RoomStatus.AVAILABLE);
        room.setAmenities(request.getAmenities());

        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            try {
                Blob photo = new SerialBlob(request.getPhoto().getBytes());
                room.setPhoto(photo);
            } catch (IOException | SQLException e) {
                throw new RuntimeException("Không thể xử lý ảnh: " + e.getMessage());
            }
        }

        return roomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        existingRoom.setRoomNumber(request.getRoomNumber());
        existingRoom.setType(request.getType());
        existingRoom.setPrice(request.getPrice());
        existingRoom.setCapacity(request.getCapacity());
        existingRoom.setDescription(request.getDescription());
        existingRoom.setStatus(request.getStatus());

        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            try {
                Blob photo = new SerialBlob(request.getPhoto().getBytes());
                existingRoom.setPhoto(photo);
            } catch (IOException | SQLException e) {
                throw new RuntimeException("Không thể xử lý ảnh: " + e.getMessage());
            }
        }

        return roomMapper.toRoomResponse(roomRepository.save(existingRoom));
    }

    @Transactional
    public void deleteRoom(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        roomRepository.deleteById(room.get().getId());
    }
}
package com.rex.hotel.service;

import com.rex.hotel.dto.request.BookingRequest;
import com.rex.hotel.dto.response.BookingResponse;
import com.rex.hotel.enums.BookingStatus;
import com.rex.hotel.enums.RoomStatus;
import com.rex.hotel.exception.AppException;
import com.rex.hotel.exception.ErrorCode;
import com.rex.hotel.mapper.BookingMapper;
import com.rex.hotel.mapper.UserMapper;
import com.rex.hotel.model.Booking;
import com.rex.hotel.model.Room;
import com.rex.hotel.model.User;
import com.rex.hotel.repository.BookingRepository;
import com.rex.hotel.repository.RoomRepository;
import com.rex.hotel.repository.UserRepository;
import com.rex.hotel.util.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BookingService {
     BookingRepository bookingRepository;
     RoomRepository roomRepository;
     UserRepository userRepository;
     BookingMapper userMapper;
     SecurityUtils  securityUtils;


    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(userMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đặt phòng với ID: " + id));
        return userMapper.toBookingResponse(booking);
    }

    public List<BookingResponse> getBookingByUserIdForUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + username));
        return bookingRepository.findByUser(user).stream()
                .map(userMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + userId));
        return bookingRepository.findByUser(user).stream()
                .map(userMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng với ID: " + roomId));
        return bookingRepository.findByRoom(room).stream()
                .map(userMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByStatus(String status) {
        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            return bookingRepository.findByStatus(bookingStatus).stream()
                    .map(userMapper::toBookingResponse)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái đặt phòng không hợp lệ: " + status);
        }
    }

    public List<BookingResponse> getBookingsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findByCheckInDateBetween(startDate, endDate).stream()
                .map(userMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse createBooking(BookingRequest request) {
        String username = securityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        // Kiểm tra trùng lịch phòng
        boolean isRoomAvailable = bookingRepository
                .findByRoomAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                        room, request.getCheckOutDate(), request.getCheckInDate()).isEmpty();

        if (!isRoomAvailable) {
            throw new IllegalStateException("Phòng đã được đặt trong khoảng thời gian này");
        }
        BigDecimal totalAmount = calculateTotalAmount(room, request.getCheckInDate(), request.getCheckOutDate());

        room.setStatus(RoomStatus.RESERVED);
        roomRepository.save(room);

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalAmount(totalAmount);
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setSpecialRequests(request.getSpecialRequests());

        return userMapper.toBookingResponse(bookingRepository.save(booking));
    }

    public BookingResponse updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đặt phòng"));

        if (BookingStatus.COMPLETED.equals(booking.getStatus()) ||
                BookingStatus.CANCELLED.equals(booking.getStatus())) {
            throw new IllegalStateException("Không thể cập nhật đặt phòng đã hoàn thành hoặc đã hủy");
        }

        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setSpecialRequests(request.getSpecialRequests());

        return userMapper.toBookingResponse(bookingRepository.save(booking));
    }


    public BookingResponse updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đặt phòng"));

        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            booking.setStatus(bookingStatus);
            return userMapper.toBookingResponse(bookingRepository.save(booking));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái đặt phòng không hợp lệ: " + status);
        }
    }


    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đặt phòng"));

        // Cập nhật trạng thái đặt phòng
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Cập nhật trạng thái phòng về AVAILABLE
        Room room = booking.getRoom();
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);

        return userMapper.toBookingResponse(booking);
    }


    public List<BookingResponse> getTodayCheckIns() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = start.plusDays(1);
        return bookingRepository.findByCheckInDateBetween(start, end).stream()
                .map(userMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getTodayCheckOuts() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = start.plusDays(1);
        return bookingRepository.findByCheckOutDateBetween(start, end).stream()
                .map(userMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalAmount(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        long days = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
        if (days <= 0) {
            throw new AppException(ErrorCode.INVALID_BOOKING_DATE); // bạn có thể định nghĩa error code riêng
        }
        return room.getPrice().multiply(BigDecimal.valueOf(days));
    }

}

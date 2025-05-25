package com.rex.hotel.repository;

import com.rex.hotel.model.Booking;
import com.rex.hotel.model.Room;
import com.rex.hotel.model.User;
import com.rex.hotel.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByRoom(Room room);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByCheckInDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Booking> findByCheckOutDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Booking> findByRoomAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            Room room, LocalDateTime checkOutDate, LocalDateTime checkInDate);

    @Query("SELECT b FROM Booking b WHERE b.room = ?1 AND b.status != 'CANCELLED' AND " +
           "((b.checkInDate <= ?2 AND b.checkOutDate >= ?3) OR " +
           "(b.checkInDate BETWEEN ?2 AND ?3) OR " +
           "(b.checkOutDate BETWEEN ?2 AND ?3))")
    List<Booking> findActiveBookingsForRoom(Room room, LocalDateTime checkInDate, LocalDateTime checkOutDate);
} 
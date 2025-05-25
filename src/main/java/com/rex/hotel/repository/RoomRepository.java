package com.rex.hotel.repository;

import com.rex.hotel.model.Room;
import com.rex.hotel.enums.RoomStatus;
import com.rex.hotel.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findByStatus(RoomStatus status);
    List<Room> findByType(RoomType type);
    
    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' AND r.type = ?1")
    List<Room> findAvailableRoomsByType(RoomType type);
    
    boolean existsByRoomNumber(String roomNumber);

    @Query("SELECT r FROM Room r WHERE r.id NOT IN " +
           "(SELECT DISTINCT b.room.id FROM Booking b " +
           "WHERE b.status != 'CANCELLED' AND " +
           "((b.checkInDate <= ?2 AND b.checkOutDate >= ?1)))")
    List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut);

    @Query("SELECT r FROM Room r WHERE r.type = ?3 AND r.id NOT IN " +
           "(SELECT DISTINCT b.room.id FROM Booking b " +
           "WHERE b.status != 'CANCELLED' AND " +
           "((b.checkInDate <= ?2 AND b.checkOutDate >= ?1)))")
    List<Room> findAvailableRoomsByType(LocalDateTime checkIn, LocalDateTime checkOut, RoomType type);

    List<Room> findByCapacityGreaterThanEqual(Integer capacity);

}
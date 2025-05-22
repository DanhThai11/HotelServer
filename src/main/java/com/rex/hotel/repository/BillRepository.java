package com.rex.hotel.repository;

import com.rex.hotel.model.Bill;
import com.rex.hotel.enums.BillStatus;
import com.rex.hotel.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Bill findByBooking(Booking booking);
    List<Bill> findByStatus(BillStatus status);
    
    @Query("SELECT b FROM Bill b WHERE b.createdAt BETWEEN ?1 AND ?2")
    List<Bill> findBillsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT b FROM Bill b WHERE b.status = 'PENDING' AND b.booking.checkOutDate <= CURRENT_DATE")
    List<Bill> findOverdueBills();
    
    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.status = 'PAID' AND b.paidAt BETWEEN ?1 AND ?2")
    Double calculateRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate);
} 
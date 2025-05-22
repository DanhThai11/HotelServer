package com.rex.hotel.repository;

import com.rex.hotel.model.Bill;
import com.rex.hotel.model.BillService;
import com.rex.hotel.model.ServiceHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BillServiceRepository extends JpaRepository<BillService, Long> {
    List<BillService> findByBill(Bill bill);
    List<BillService> findByService(ServiceHotel serviceHotel);
    
    @Query("SELECT bs.service, SUM(bs.quantity) as totalUsage FROM BillService bs GROUP BY bs.service ORDER BY totalUsage DESC")
    List<Object[]> findMostUsedServices();
} 
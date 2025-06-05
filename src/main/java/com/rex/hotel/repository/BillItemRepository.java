package com.rex.hotel.repository;

import com.rex.hotel.model.Bill;
import com.rex.hotel.model.BillItem;
import com.rex.hotel.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {
    List<BillItem> findByBill(Bill bill);
    List<BillItem> findByItem(InventoryItem item);
    
    @Query("SELECT bi.item, SUM(bi.quantity) as totalQuantity FROM BillItem bi GROUP BY bi.item ORDER BY totalQuantity DESC")
    List<Object[]> findMostConsumedItems();
} 
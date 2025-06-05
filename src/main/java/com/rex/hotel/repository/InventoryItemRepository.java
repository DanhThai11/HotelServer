package com.rex.hotel.repository;

import com.rex.hotel.model.InventoryItem;
import com.rex.hotel.enums.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByCategory(ItemCategory category);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.minQuantity")
    List<InventoryItem> findLowStockItems();
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity = 0")
    List<InventoryItem> findOutOfStockItems();
    
    List<InventoryItem> findBySupplier(String supplier);
} 
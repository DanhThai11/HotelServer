package com.rex.hotel.repository;

import com.rex.hotel.model.ServiceHotel;
import com.rex.hotel.enums.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceHotel, Long> {
    List<ServiceHotel> findByCategory(ServiceCategory category);
    List<ServiceHotel> findByIsAvailable(Boolean isAvailable);
    List<ServiceHotel> findByCategoryAndIsAvailable(ServiceCategory category, Boolean isAvailable);
} 
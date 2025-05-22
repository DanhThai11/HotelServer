package com.rex.hotel.service;

import com.rex.hotel.dto.request.ServiceRequest;
import com.rex.hotel.dto.response.ServiceResponse;
import com.rex.hotel.enums.ServiceCategory;
import com.rex.hotel.mapper.ServiceMapper;
import com.rex.hotel.model.ServiceHotel;
import com.rex.hotel.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class HotelService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());
    }

    public ServiceResponse getServiceById(Long id) {
        ServiceHotel service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy dịch vụ với ID: " + id));
        return serviceMapper.toServiceResponse(service);
    }

    public List<ServiceResponse> getServicesByCategory(String category) {
        try {
            ServiceCategory serviceCategory = ServiceCategory.valueOf(category.toUpperCase());
            return serviceRepository.findByCategory(serviceCategory)
                    .stream()
                    .map(serviceMapper::toServiceResponse)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Danh mục dịch vụ không hợp lệ: " + category);
        }
    }

    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        ServiceHotel service = serviceMapper.toServiceHotel(request);
        return serviceMapper.toServiceResponse(serviceRepository.save(service));
    }

    @Transactional
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        ServiceHotel existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy dịch vụ với ID: " + id));

        serviceMapper.updateServiceFromRequest(request, existingService);
        return serviceMapper.toServiceResponse(serviceRepository.save(existingService));
    }

    @Transactional
    public void deleteService(Long id) {
        ServiceHotel service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy dịch vụ với ID: " + id));
        serviceRepository.delete(service);
    }
}

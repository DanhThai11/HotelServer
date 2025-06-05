package com.rex.hotel.service;


import com.rex.hotel.dto.request.ServiceRequest;
import com.rex.hotel.dto.response.ServiceResponse;
import com.rex.hotel.mapper.ServiceMapper;
import com.rex.hotel.model.ServiceHotel;
import com.rex.hotel.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        ServiceHotel service = ServiceHotel.builder()
                .name(request.getName())
                .category(request.getCategory())
                .price(request.getPrice())
                .description(request.getDescription())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true) // nếu client không truyền thì default true
                .build();
        return serviceMapper.toServiceResponse(serviceRepository.save(service));
    }
}

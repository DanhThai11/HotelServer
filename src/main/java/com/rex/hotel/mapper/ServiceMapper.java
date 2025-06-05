package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.ServiceRequest;

import com.rex.hotel.dto.response.ServiceResponse;
import com.rex.hotel.model.ServiceHotel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceHotel toServiceHotel(ServiceRequest request);
    ServiceResponse toServiceResponse(ServiceHotel service);

    void updateServiceFromRequest(ServiceRequest request, @MappingTarget ServiceHotel service);
}

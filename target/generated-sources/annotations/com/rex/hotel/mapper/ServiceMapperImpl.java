package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.ServiceRequest;
import com.rex.hotel.dto.response.ServiceResponse;
import com.rex.hotel.model.ServiceHotel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-06T00:23:02+0700",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public ServiceHotel toServiceHotel(ServiceRequest request) {
        if ( request == null ) {
            return null;
        }

        ServiceHotel.ServiceHotelBuilder serviceHotel = ServiceHotel.builder();

        serviceHotel.category( request.getCategory() );
        serviceHotel.description( request.getDescription() );
        serviceHotel.isAvailable( request.getIsAvailable() );
        serviceHotel.name( request.getName() );
        serviceHotel.price( request.getPrice() );

        return serviceHotel.build();
    }

    @Override
    public ServiceResponse toServiceResponse(ServiceHotel service) {
        if ( service == null ) {
            return null;
        }

        ServiceResponse.ServiceResponseBuilder serviceResponse = ServiceResponse.builder();

        serviceResponse.category( service.getCategory() );
        serviceResponse.description( service.getDescription() );
        serviceResponse.id( service.getId() );
        serviceResponse.name( service.getName() );
        serviceResponse.price( service.getPrice() );

        return serviceResponse.build();
    }

    @Override
    public void updateServiceFromRequest(ServiceRequest request, ServiceHotel service) {
        if ( request == null ) {
            return;
        }

        service.setCategory( request.getCategory() );
        service.setDescription( request.getDescription() );
        service.setIsAvailable( request.getIsAvailable() );
        service.setName( request.getName() );
        service.setPrice( request.getPrice() );
    }
}

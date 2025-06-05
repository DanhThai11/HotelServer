package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.ServiceRequest;
import com.rex.hotel.dto.response.ServiceResponse;
import com.rex.hotel.model.ServiceHotel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-05T21:21:00+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public ServiceHotel toServiceHotel(ServiceRequest request) {
        if ( request == null ) {
            return null;
        }

        ServiceHotel.ServiceHotelBuilder serviceHotel = ServiceHotel.builder();

        serviceHotel.name( request.getName() );
        serviceHotel.category( request.getCategory() );
        serviceHotel.price( request.getPrice() );
        serviceHotel.description( request.getDescription() );
        serviceHotel.isAvailable( request.getIsAvailable() );

        return serviceHotel.build();
    }

    @Override
    public ServiceResponse toServiceResponse(ServiceHotel service) {
        if ( service == null ) {
            return null;
        }

        ServiceResponse.ServiceResponseBuilder serviceResponse = ServiceResponse.builder();

        serviceResponse.id( service.getId() );
        serviceResponse.name( service.getName() );
        serviceResponse.description( service.getDescription() );
        serviceResponse.price( service.getPrice() );
        serviceResponse.category( service.getCategory() );

        return serviceResponse.build();
    }

    @Override
    public void updateServiceFromRequest(ServiceRequest request, ServiceHotel service) {
        if ( request == null ) {
            return;
        }

        service.setName( request.getName() );
        service.setCategory( request.getCategory() );
        service.setPrice( request.getPrice() );
        service.setDescription( request.getDescription() );
        service.setIsAvailable( request.getIsAvailable() );
    }
}

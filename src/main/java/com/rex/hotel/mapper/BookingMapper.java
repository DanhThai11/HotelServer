package com.rex.hotel.mapper;

import com.rex.hotel.dto.response.BookingResponse;
import com.rex.hotel.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "room.type", target = "roomType")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    BookingResponse toBookingResponse(Booking booking);
}

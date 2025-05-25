package com.rex.hotel.mapper;

import com.rex.hotel.dto.response.BookingResponse;
import com.rex.hotel.enums.RoomType;
import com.rex.hotel.model.Booking;
import com.rex.hotel.model.Room;
import com.rex.hotel.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-25T04:09:08+0700",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingResponse toBookingResponse(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingResponse.BookingResponseBuilder bookingResponse = BookingResponse.builder();

        bookingResponse.roomId( bookingRoomId( booking ) );
        bookingResponse.user( userToUserSummary( booking.getUser() ) );
        bookingResponse.roomType( bookingRoomType( booking ) );
        bookingResponse.roomNumber( bookingRoomRoomNumber( booking ) );
        bookingResponse.checkInDate( booking.getCheckInDate() );
        bookingResponse.checkOutDate( booking.getCheckOutDate() );
        bookingResponse.createdAt( booking.getCreatedAt() );
        bookingResponse.id( booking.getId() );
        bookingResponse.numberOfGuests( booking.getNumberOfGuests() );
        bookingResponse.specialRequests( booking.getSpecialRequests() );
        bookingResponse.status( booking.getStatus() );
        bookingResponse.totalAmount( booking.getTotalAmount() );
        bookingResponse.updatedAt( booking.getUpdatedAt() );

        return bookingResponse.build();
    }

    private Long bookingRoomId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Room room = booking.getRoom();
        if ( room == null ) {
            return null;
        }
        Long id = room.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected BookingResponse.UserSummary userToUserSummary(User user) {
        if ( user == null ) {
            return null;
        }

        BookingResponse.UserSummary.UserSummaryBuilder userSummary = BookingResponse.UserSummary.builder();

        userSummary.id( user.getId() );
        userSummary.username( user.getUsername() );

        return userSummary.build();
    }

    private RoomType bookingRoomType(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Room room = booking.getRoom();
        if ( room == null ) {
            return null;
        }
        RoomType type = room.getType();
        if ( type == null ) {
            return null;
        }
        return type;
    }

    private String bookingRoomRoomNumber(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Room room = booking.getRoom();
        if ( room == null ) {
            return null;
        }
        String roomNumber = room.getRoomNumber();
        if ( roomNumber == null ) {
            return null;
        }
        return roomNumber;
    }
}

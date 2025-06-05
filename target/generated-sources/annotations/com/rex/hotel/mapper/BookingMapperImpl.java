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
    date = "2025-06-05T21:21:00+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.2 (Eclipse Adoptium)"
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
        bookingResponse.id( booking.getId() );
        bookingResponse.checkInDate( booking.getCheckInDate() );
        bookingResponse.checkOutDate( booking.getCheckOutDate() );
        bookingResponse.numberOfGuests( booking.getNumberOfGuests() );
        bookingResponse.totalAmount( booking.getTotalAmount() );
        bookingResponse.specialRequests( booking.getSpecialRequests() );
        bookingResponse.status( booking.getStatus() );
        bookingResponse.createdAt( booking.getCreatedAt() );
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

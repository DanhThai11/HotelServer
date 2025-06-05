package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.RoomRequest;
import com.rex.hotel.dto.response.RoomResponse;
import com.rex.hotel.model.Room;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T23:53:02+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public RoomResponse toRoomResponse(Room room) {
        if ( room == null ) {
            return null;
        }

        RoomResponse.RoomResponseBuilder roomResponse = RoomResponse.builder();

        roomResponse.id( room.getId() );
        roomResponse.description( room.getDescription() );
        roomResponse.price( room.getPrice() );
        if ( room.getType() != null ) {
            roomResponse.type( room.getType().name() );
        }
        if ( room.getCapacity() != null ) {
            roomResponse.capacity( room.getCapacity() );
        }
        if ( room.getStatus() != null ) {
            roomResponse.status( room.getStatus().name() );
        }
        roomResponse.roomNumber( room.getRoomNumber() );

        roomResponse.photo( convertBlobToBase64(room.getPhoto()) );

        return roomResponse.build();
    }

    @Override
    public Room toRoom(RoomRequest roomRequest) {
        if ( roomRequest == null ) {
            return null;
        }

        Room.RoomBuilder room = Room.builder();

        room.roomNumber( roomRequest.getRoomNumber() );
        room.type( roomRequest.getType() );
        room.price( roomRequest.getPrice() );
        room.status( roomRequest.getStatus() );
        List<String> list = roomRequest.getAmenities();
        if ( list != null ) {
            room.amenities( new ArrayList<String>( list ) );
        }
        room.capacity( roomRequest.getCapacity() );
        room.description( roomRequest.getDescription() );

        return room.build();
    }
}

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
    date = "2025-06-06T08:19:19+0700",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public RoomResponse toRoomResponse(Room room) {
        if ( room == null ) {
            return null;
        }

        RoomResponse.RoomResponseBuilder roomResponse = RoomResponse.builder();

        if ( room.getCapacity() != null ) {
            roomResponse.capacity( room.getCapacity() );
        }
        roomResponse.description( room.getDescription() );
        roomResponse.id( room.getId() );
        roomResponse.price( room.getPrice() );
        roomResponse.roomNumber( room.getRoomNumber() );
        if ( room.getStatus() != null ) {
            roomResponse.status( room.getStatus().name() );
        }
        if ( room.getType() != null ) {
            roomResponse.type( room.getType().name() );
        }

        roomResponse.photo( convertBlobToBase64(room.getPhoto()) );

        return roomResponse.build();
    }

    @Override
    public Room toRoom(RoomRequest roomRequest) {
        if ( roomRequest == null ) {
            return null;
        }

        Room.RoomBuilder room = Room.builder();

        List<String> list = roomRequest.getAmenities();
        if ( list != null ) {
            room.amenities( new ArrayList<String>( list ) );
        }
        room.capacity( roomRequest.getCapacity() );
        room.description( roomRequest.getDescription() );
        room.price( roomRequest.getPrice() );
        room.roomNumber( roomRequest.getRoomNumber() );
        room.status( roomRequest.getStatus() );
        room.type( roomRequest.getType() );

        return room.build();
    }
}

package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.RegisterRequest;
import com.rex.hotel.dto.request.RoomRequest;
import com.rex.hotel.dto.request.UserUpdateRequest;
import com.rex.hotel.dto.response.RoomResponse;
import com.rex.hotel.dto.response.UserResponse;
import com.rex.hotel.model.Room;
import com.rex.hotel.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {
        RoomResponse toRoomResponse(Room room);
        Room toRoom(RoomRequest roomRequest);
}

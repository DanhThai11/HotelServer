package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.RegisterRequest;
import com.rex.hotel.dto.request.RoomRequest;
import com.rex.hotel.dto.request.UserUpdateRequest;
import com.rex.hotel.dto.response.RoomResponse;
import com.rex.hotel.dto.response.UserResponse;
import com.rex.hotel.model.Room;
import com.rex.hotel.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@Mapper(componentModel = "spring")
@Component
public interface RoomMapper {
    @Mapping(target = "photo", expression = "java(convertBlobToBase64(room.getPhoto()))")
    RoomResponse toRoomResponse(Room room);
    
    @Mapping(target = "photo", ignore = true)
    Room toRoom(RoomRequest roomRequest);

    default String convertBlobToBase64(Blob photo) {
        if (photo == null) {
            return null;
        }
        try {
            byte[] bytes = photo.getBytes(1, (int) photo.length());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (SQLException e) {
            throw new RuntimeException("Không thể chuyển đổi ảnh: " + e.getMessage());
        }
    }
}

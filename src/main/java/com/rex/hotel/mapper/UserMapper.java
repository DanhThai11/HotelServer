package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.RegisterRequest;
import com.rex.hotel.dto.request.UserUpdateRequest;
import com.rex.hotel.dto.response.BookingResponse;
import com.rex.hotel.dto.response.UserResponse;
import com.rex.hotel.model.Booking;
import com.rex.hotel.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper{
        User toUser(RegisterRequest request);
        UserResponse toUserResponse(User user);
        void toUpdateUser(@MappingTarget User user, UserUpdateRequest request);
}

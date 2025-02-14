package com.project.hotel.mapper;

import com.project.hotel.dto.response.RoomInfoResponse;
import com.project.hotel.entity.RoomEntity;

public class RoomMapper {

    public static RoomInfoResponse toRoomDTO(RoomEntity roomEntity) {
        RoomInfoResponse response = new RoomInfoResponse();
        response.setId(roomEntity.getId());
        response.setRoomNumber(roomEntity.getRoomNumber());
        response.setRoomType(roomEntity.getRoomType());
        response.setCapacity(roomEntity.getCapacity());
        response.setIsAvailable(roomEntity.getIsAvailable());
        response.setHotelName(roomEntity.getHotel().getName());
        response.setPricePerNight(roomEntity.getPricePerNight());

        return response;
    }
}

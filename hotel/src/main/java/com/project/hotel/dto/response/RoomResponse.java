package com.project.hotel.dto.response;

import com.project.hotel.enums.RoomType;
import lombok.Data;

@Data
public class RoomResponse {

    private Integer roomNumber;
    private RoomType roomType;
    private String hotelName;
}

package com.project.hotel.service;

import com.project.hotel.dto.request.RoomRequest;
import com.project.hotel.dto.request.RoomUpdateRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.RoomInfoResponse;
import com.project.hotel.dto.response.RoomResponse;
import com.project.hotel.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoomService {

    ResponseEntity<RoomResponse> create(RoomRequest request);
    PageResponse<RoomInfoResponse> getAllRooms(int page, int size);
    ResponseEntity<RoomInfoResponse> findRoomById(Long roomId);
    ResponseEntity<List<RoomInfoResponse>> findRoomsByHotel(Long hotelId);
    ResponseEntity<RoomResponse> update(Long roomId, RoomUpdateRequest request);
    void delete(Long roomId);
    UserEntity getCurrentUser();
}

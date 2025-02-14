package com.project.hotel.service;

import com.project.hotel.dto.request.HotelRequest;
import com.project.hotel.dto.response.HotelInfoResponse;
import com.project.hotel.dto.response.HotelResponse;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HotelService {

    ResponseEntity<HotelResponse> create(HotelRequest request);
    PageResponse<HotelInfoResponse> findAllHotels(int page, int size);
    List<HotelInfoResponse> findHotelByName(String name);
    ResponseEntity<HotelInfoResponse> findHotelById(Long hotelId);
    ResponseEntity<HotelResponse> update(Long hotelId, HotelRequest request);
    void delete(Long hotelId);
    UserEntity getCurrentUser();
}

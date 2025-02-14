package com.project.hotel.mapper;

import com.project.hotel.dto.response.HotelInfoResponse;
import com.project.hotel.entity.HotelEntity;

public class HotelMapper {

    public static HotelInfoResponse toHotelDTO(HotelEntity hotel) {
        HotelInfoResponse response = new HotelInfoResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setLocation(hotel.getLocation());
        response.setRating(hotel.getRating());
        response.setDescription(hotel.getDescription());
        response.setNumberOfRooms(hotel.getNumberOfRooms());
        response.setAvailableRooms(hotel.getAvailableRooms());
        response.setHasPool(hotel.isHasPool());
        response.setHasGym(hotel.isHasGym());
        response.setHasWiFi(hotel.isHasWiFi());
        response.setHasRestaurant(hotel.isHasRestaurant());
        response.setHasParking(hotel.isHasParking());
        response.setHasSpa(hotel.isHasSpa());
        response.setContactNumber(hotel.getContactNumber());
        response.setEmail(hotel.getEmail());
        response.setWebsite(hotel.getWebsite());
        response.setCheckInTime(hotel.getCheckInTime());
        response.setCheckOutTime(hotel.getCheckOutTime());
        response.setCity(hotel.getCity());
        response.setState(hotel.getState());
        response.setCountry(hotel.getCountry());
        response.setCreatedAt(hotel.getCreatedAt());
        response.setUpdatedAt(hotel.getUpdatedAt());

        return response;
    }
}

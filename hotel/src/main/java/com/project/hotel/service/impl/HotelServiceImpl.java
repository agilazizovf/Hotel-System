package com.project.hotel.service.impl;

import com.project.hotel.dto.request.HotelRequest;
import com.project.hotel.dto.response.HotelInfoResponse;
import com.project.hotel.dto.response.HotelResponse;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.entity.HotelEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.exception.CustomException;
import com.project.hotel.mapper.HotelMapper;
import com.project.hotel.repository.HotelRepository;
import com.project.hotel.repository.UserRepository;
import com.project.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper mapper;
    @Override
    public ResponseEntity<HotelResponse> create(HotelRequest request) {
        UserEntity user = getCurrentUser();

        HotelEntity hotel = new HotelEntity();
        mapper.map(request, hotel);
        hotel.setRating(0);
        hotel.setNumberOfRooms(0);
        hotel.setAvailableRooms(0);
        hotel.setCreatedAt(LocalDateTime.now());
        hotel.setUpdatedAt(LocalDateTime.now());
        hotel.setUser(user);
        hotelRepository.save(hotel);

        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public PageResponse<HotelInfoResponse> findAllHotels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HotelEntity> hotelEntities = hotelRepository.findAll(pageable);

        List<HotelInfoResponse> responses = hotelEntities
                .stream()
                .map(HotelMapper::toHotelDTO)
                .collect(Collectors.toList());

        PageResponse<HotelInfoResponse> pageResponse = getHotelInfoResponsePageResponse(responses, hotelEntities);

        return pageResponse;
    }

    @Override
    public List<HotelInfoResponse> findHotelByName(String name) {
        List<HotelEntity> hotels = hotelRepository.findHotelByNameContainingIgnoreCase(name);
        List<HotelInfoResponse> responses = hotels
                .stream()
                .map(hotel -> {
                    HotelInfoResponse response = new HotelInfoResponse();
                    mapper.map(hotel, response);
                    return response;
                })
                .toList();

        return responses;
    }

    private static PageResponse<HotelInfoResponse> getHotelInfoResponsePageResponse(List<HotelInfoResponse> responses, Page<HotelEntity> hotelEntities) {
        PageResponse<HotelInfoResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(responses);
        pageResponse.setPage(hotelEntities.getPageable().getPageNumber());
        pageResponse.setSize(hotelEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(hotelEntities.getTotalElements());
        pageResponse.setTotalPages(hotelEntities.getTotalPages());
        pageResponse.setLast(hotelEntities.isLast());
        pageResponse.setFirst(hotelEntities.isFirst());
        return pageResponse;
    }

    @Override
    public ResponseEntity<HotelInfoResponse> findHotelById(Long hotelId) {
        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));
        HotelInfoResponse response = new HotelInfoResponse();
        mapper.map(hotel, response);

        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @Override
    public ResponseEntity<HotelResponse> update(Long hotelId, HotelRequest request) {
        UserEntity user = getCurrentUser();

        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));
        if (!hotel.getUser().equals(user)) {
            throw new CustomException("Bu hotel məlumatlarını dəyişməyə icazəniz yoxdur",
                    "You do not have permission to update this hotel", "Not Authorized", 403, null);
        }
        mapper.map(request, hotel);
        hotel.setUser(user);
        hotelRepository.save(hotel);

        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());

        return ResponseEntity.ok(response);
    }

    @Override
    public void delete(Long hotelId) {
        UserEntity user = getCurrentUser();

        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));
        if (!hotel.getUser().equals(user)) {
            throw new CustomException("Bu hotel məlumatlarını silməyə icazəniz yoxdur",
                    "You do not have permission to delete this hotel", "Not Authorized", 403, null);
        }
        hotelRepository.deleteById(hotel.getId());
    }

    @Override
    public UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("İstifadəçi tapılmadı", "User not found",
                        "Not found", 404, null));
    }
}

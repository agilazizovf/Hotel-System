package com.project.hotel.service.impl;

import com.project.hotel.dto.request.RoomRequest;
import com.project.hotel.dto.request.RoomUpdateRequest;
import com.project.hotel.dto.response.HotelInfoResponse;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.RoomInfoResponse;
import com.project.hotel.dto.response.RoomResponse;
import com.project.hotel.entity.HotelEntity;
import com.project.hotel.entity.RoomEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.exception.CustomException;
import com.project.hotel.mapper.HotelMapper;
import com.project.hotel.mapper.RoomMapper;
import com.project.hotel.repository.HotelRepository;
import com.project.hotel.repository.RoomRepository;
import com.project.hotel.repository.UserRepository;
import com.project.hotel.service.RoomService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public ResponseEntity<RoomResponse> create(RoomRequest request) {
        UserEntity user = getCurrentUser();
        HotelEntity hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found", 404, null));

        boolean roomExists = roomRepository.existsByRoomNumberAndHotel(request.getRoomNumber(), hotel);
        if (roomExists) {
            throw new CustomException("Bu otaq nömrəsi artıq mövcuddur", "Room number already exists", "Conflict", 409, null);
        }

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomType(request.getRoomType());
        roomEntity.setRoomNumber(request.getRoomNumber());
        roomEntity.setCapacity(request.getCapacity());
        roomEntity.setPricePerNight(request.getPricePerNight());

        roomEntity.setHotel(hotel);
        roomEntity.setIsAvailable(true);
        roomEntity.setUser(user);

        try {
            roomRepository.save(roomEntity);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomException("Room was updated by another transaction", "Optimistic locking failure", "Conflict", 409, null);
        }


        hotel.setNumberOfRooms(hotel.getNumberOfRooms() + 1);
        if (roomEntity.getIsAvailable()) {
            hotel.setAvailableRooms(hotel.getAvailableRooms() + 1);
        }

        hotelRepository.save(hotel);

        RoomResponse response = new RoomResponse();
        response.setHotelName(hotel.getName());
        response.setRoomType(roomEntity.getRoomType());
        response.setRoomNumber(roomEntity.getRoomNumber());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public PageResponse<RoomInfoResponse> getAllRooms(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new CustomException("Səhifə və ya ölçü etibarsızdır", "Invalid page or size",
                    "Bad Request", 400, null);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomEntity> roomEntities = roomRepository.findAll(pageable);

        List<RoomInfoResponse> responses = roomEntities
                .stream()
                .map(RoomMapper::toRoomDTO)
                .collect(Collectors.toList());

        PageResponse<RoomInfoResponse> pageResponse = getRoomInfoResponsePageResponse(responses, roomEntities);

        return pageResponse;
    }
    private static PageResponse<RoomInfoResponse> getRoomInfoResponsePageResponse(List<RoomInfoResponse> responses, Page<RoomEntity> roomEntities) {
        PageResponse<RoomInfoResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(responses);
        pageResponse.setPage(roomEntities.getPageable().getPageNumber());
        pageResponse.setSize(roomEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(roomEntities.getTotalElements());
        pageResponse.setTotalPages(roomEntities.getTotalPages());
        pageResponse.setLast(roomEntities.isLast());
        pageResponse.setFirst(roomEntities.isFirst());
        return pageResponse;
    }


    @Override
    public ResponseEntity<RoomInfoResponse> findRoomById(Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("Otaq tapılmadı", "Room not found", "Not found",
                        404, null));
        RoomInfoResponse response = new RoomInfoResponse();
        mapper.map(room, response);

        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @Override
    public ResponseEntity<List<RoomInfoResponse>> findRoomsByHotel(Long hotelId) {
        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));

        List<RoomEntity> roomEntities = roomRepository.findByHotel(hotel);

        if (roomEntities.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<RoomInfoResponse> responses = roomEntities
                .stream()
                .map(RoomMapper::toRoomDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Transactional
    @Override
    public ResponseEntity<RoomResponse> update(Long roomId, RoomUpdateRequest request) {
        UserEntity user = getCurrentUser();

        HotelEntity hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));

        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("Otaq tapılmadı", "Room not found", "Not found",
                        404, null));

        if (!room.getUser().equals(user)) {
            throw new CustomException("Bu otaq məlumatlarını dəyişməyə icazəniz yoxdur",
                    "You do not have permission to update this room", "Not Authorized", 403, null);
        }

        boolean roomExists = roomRepository.existsByRoomNumberAndHotel(request.getRoomNumber(), hotel);
        if (roomExists) {
            throw new CustomException("Bu otaq nömrəsi artıq mövcuddur", "Room number already exists", "Conflict", 409, null);
        }

        boolean wasAvailable = room.getIsAvailable();
        mapper.map(request, room);

        if (wasAvailable != room.getIsAvailable()) {
            if (room.getIsAvailable()) {
                hotel.setAvailableRooms(hotel.getAvailableRooms() + 1);
            } else {
                hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
            }
        }
        hotelRepository.save(hotel);
        roomRepository.save(room);

        RoomResponse response = new RoomResponse();
        response.setHotelName(hotel.getName());
        response.setRoomType(room.getRoomType());
        response.setRoomNumber(room.getRoomNumber());

        return ResponseEntity.ok(response);
    }

    @Override
    public void delete(Long roomId) {
        UserEntity user = getCurrentUser();
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("Otaq tapılmadı", "Room not found", "Not found",
                        404, null));

        if (!room.getUser().equals(user)) {
            throw new CustomException("Bu otaq məlumatlarını dəyişməyə icazəniz yoxdur",
                    "You do not have permission to update this room", "Not Authorized", 403, null);
        }
        HotelEntity hotel = room.getHotel();
        hotel.setNumberOfRooms(hotel.getNumberOfRooms() - 1);
        if (room.getIsAvailable()) {
            hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
        }
        hotelRepository.save(hotel); // Save the updated hotel

        roomRepository.deleteById(room.getId());
    }

    @Override
    public UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("İstifadəçi tapılmadı", "User not found",
                        "Not found", 404, null));
    }
}

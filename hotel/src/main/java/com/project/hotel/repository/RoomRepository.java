package com.project.hotel.repository;

import com.project.hotel.entity.HotelEntity;
import com.project.hotel.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Page<RoomEntity> findAll(Pageable pageable);
    List<RoomEntity> findByHotel(HotelEntity hotel);
    boolean existsByRoomNumberAndHotel(Integer roomNumber, HotelEntity hotel);
}

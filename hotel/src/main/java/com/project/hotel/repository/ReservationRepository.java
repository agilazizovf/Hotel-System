package com.project.hotel.repository;

import com.project.hotel.entity.ReservationEntity;
import com.project.hotel.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    Optional<ReservationEntity> findByRoomAndCheckInDateBeforeAndCheckOutDateAfter(RoomEntity room, LocalDate checkOutDate, LocalDate checkInDate);
    List<ReservationEntity> findByUserEmail(String username);
    Page<ReservationEntity> findAll(Pageable pageable);
}

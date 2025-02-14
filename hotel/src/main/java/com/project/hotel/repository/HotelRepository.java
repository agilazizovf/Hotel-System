package com.project.hotel.repository;

import com.project.hotel.entity.HotelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    Page<HotelEntity> findAll(Pageable pageable);

    List<HotelEntity> findHotelByNameContainingIgnoreCase(String name);
}

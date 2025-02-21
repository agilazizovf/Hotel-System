package com.project.hotel.repository;

import com.project.hotel.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Page<ReviewEntity> findAll(Pageable pageable);

    boolean existsByUserIdAndHotelIdAndRoomId(Long id, Long hotelId, Long roomId);
}

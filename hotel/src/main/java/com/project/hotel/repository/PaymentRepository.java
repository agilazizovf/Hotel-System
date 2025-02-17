package com.project.hotel.repository;

import com.project.hotel.entity.HotelEntity;
import com.project.hotel.entity.PaymentEntity;
import com.project.hotel.entity.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Page<PaymentEntity> findAll(Pageable pageable);
    List<PaymentEntity> findByReservation_Hotel(HotelEntity hotel);

    List<PaymentEntity> findByReservation(ReservationEntity reservation);

}

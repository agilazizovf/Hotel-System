package com.project.hotel.repository;

import com.project.hotel.entity.PictureContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureContentRepository extends JpaRepository<PictureContentEntity, Long> {
}

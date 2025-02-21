package com.project.hotel.service.impl;

import com.project.hotel.dto.request.ReviewRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.ReservationAcceptedResponse;
import com.project.hotel.dto.response.ReviewResponse;
import com.project.hotel.entity.*;
import com.project.hotel.exception.CustomException;
import com.project.hotel.mapper.ReservationMapper;
import com.project.hotel.mapper.ReviewMapper;
import com.project.hotel.repository.HotelRepository;
import com.project.hotel.repository.ReviewRepository;
import com.project.hotel.repository.RoomRepository;
import com.project.hotel.repository.UserRepository;
import com.project.hotel.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper mapper;

    @Override
    public ResponseEntity<ReviewResponse> add(ReviewRequest request) {
        UserEntity user = getCurrentUser();
        HotelEntity hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));
        RoomEntity room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException("Otaq tapılmadı", "Room not found", "Not found",
                        404, null));
        boolean reviewExists = reviewRepository.existsByUserIdAndHotelIdAndRoomId(user.getId(), hotel.getId(), room.getId());
        if (reviewExists) {
            throw new IllegalStateException("You have already reviewed this room.");
        }
        ReviewEntity review = new ReviewEntity();
        review.setTitle(request.getTitle());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        review.setHotel(hotel);
        review.setRoom(room);
        review.setUser(user);

        reviewRepository.save(review);

        ReviewResponse response = new ReviewResponse();
        mapper.map(review, response);
        response.setHotelName(hotel.getName());
        response.setRoomNumber(room.getRoomNumber());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public PageResponse<ReviewResponse> getAllReviews(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new CustomException("Səhifə və ya ölçü etibarsızdır", "Invalid page or size",
                    "Bad Request", 400, null);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewEntity> reviewEntities = reviewRepository.findAll(pageable);

        List<ReviewResponse> responses = reviewEntities
                .stream()
                .map(ReviewMapper::toReviewDTO)
                .collect(Collectors.toList());

        return getReviewPageResponse(responses, reviewEntities);
    }

    private static PageResponse<ReviewResponse> getReviewPageResponse(
            List<ReviewResponse> responses, Page<ReviewEntity> reviewEntities) {

        PageResponse<ReviewResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(responses);
        pageResponse.setPage(reviewEntities.getPageable().getPageNumber());
        pageResponse.setSize(reviewEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(reviewEntities.getTotalElements());
        pageResponse.setTotalPages(reviewEntities.getTotalPages());
        pageResponse.setLast(reviewEntities.isLast());
        pageResponse.setFirst(reviewEntities.isFirst());

        return pageResponse;
    }

    @Override
    public ResponseEntity<ReviewResponse> findReviewById(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException("Rəy tapılmadı", "Review not found", "Not found",
                        404, null));
        ReviewResponse response = new ReviewResponse();
        response.setTitle(review.getTitle());
        response.setComment(review.getComment());
        response.setRating(review.getRating());
        response.setHotelName(review.getHotel().getName());
        response.setRoomNumber(review.getRoom().getRoomNumber());

        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @Override
    public ResponseEntity<ReviewResponse> update(Long reviewId, ReviewRequest request) {
        UserEntity user = getCurrentUser();
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException("Rəy tapılmadı", "Review not found", "Not found",
                        404, null));
        HotelEntity hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));
        RoomEntity room = roomRepository.findById(request.getHotelId())
                .orElseThrow(() -> new CustomException("Otaq tapılmadı", "Room not found", "Not found",
                        404, null));
        if (!review.getUser().equals(user)) {
            throw new IllegalStateException("You do not have permission to update this review");
        }

        review.setTitle(request.getTitle());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        review.setHotel(hotel);
        review.setRoom(room);
        review.setUser(user);

        reviewRepository.save(review);

        ReviewResponse response = new ReviewResponse();
        mapper.map(review, response);
        response.setHotelName(hotel.getName());
        response.setRoomNumber(room.getRoomNumber());

        return ResponseEntity.ok(response);
    }

    @Override
    public void delete(Long reviewId) {
        UserEntity user = getCurrentUser();
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException("Rəy tapılmadı", "Review not found", "Not found",
                        404, null));
        if (!review.getUser().equals(user)) {
            throw new IllegalStateException("You do not have permission to update this review");
        }
        reviewRepository.deleteById(review.getId());
    }

    @Override
    public UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("İstifadəçi tapılmadı", "User not found",
                        "Not found", 404, null));
    }
}

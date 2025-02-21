package com.project.hotel.controller;

import com.project.hotel.dto.request.HotelRequest;
import com.project.hotel.dto.response.HotelInfoResponse;
import com.project.hotel.dto.response.HotelResponse;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADD_HOTEL')")
    public ResponseEntity<HotelResponse> create(@RequestBody @Valid HotelRequest request) {
        return hotelService.create(request);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('GET_ALL_HOTELS')")
    public PageResponse<HotelInfoResponse> getAllHotels(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return hotelService.findAllHotels(page, size);
    }

    @GetMapping("/search-by-name")
    @PreAuthorize("hasAuthority('SEARCH_HOTEL')")
    public List<HotelInfoResponse> findHotelByName(@RequestParam(name = "search") String name) {
        return hotelService.findHotelByName(name);
    }

    @GetMapping("/{hotelId}")
    @PreAuthorize("hasAuthority('FIND_HOTEL_BY_ID')")
    public ResponseEntity<HotelInfoResponse> findHotelById(@PathVariable Long hotelId) {
        return hotelService.findHotelById(hotelId);
    }

    @PutMapping("/update/{hotelId}")
    @PreAuthorize("hasAuthority('UPDATE_HOTEL')")
    public ResponseEntity<HotelResponse> update(@PathVariable Long hotelId, @RequestBody @Valid HotelRequest request) {
        return hotelService.update(hotelId, request);
    }

    @DeleteMapping("/delete/{hotelId}")
    @PreAuthorize("hasAuthority('DELETE_HOTEL')")
    public void delete(@PathVariable Long hotelId) {
        hotelService.delete(hotelId);
    }
}

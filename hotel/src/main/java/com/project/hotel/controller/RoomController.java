package com.project.hotel.controller;

import com.project.hotel.dto.request.RoomRequest;
import com.project.hotel.dto.request.RoomUpdateRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.RoomInfoResponse;
import com.project.hotel.dto.response.RoomResponse;
import com.project.hotel.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<RoomResponse> create(@RequestBody @Valid RoomRequest request) {
        return roomService.create(request);
    }

    @GetMapping("/get-all")
    public PageResponse<RoomInfoResponse> getAllRooms(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return roomService.getAllRooms(page, size);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomInfoResponse> findRoomById(@PathVariable Long roomId) {
        return roomService.findRoomById(roomId);
    }

    @GetMapping("/find-by-hotel/{hotelId}")
    public ResponseEntity<List<RoomInfoResponse>> findRoomsByHotel(@PathVariable Long hotelId) {
        return roomService.findRoomsByHotel(hotelId);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> update(@PathVariable Long roomId, @RequestBody @Valid RoomUpdateRequest request) {
        return roomService.update(roomId, request);
    }

    @DeleteMapping("/delete/{roomId}")
    public void delete(@PathVariable Long roomId) {
        roomService.delete(roomId);
    }
}

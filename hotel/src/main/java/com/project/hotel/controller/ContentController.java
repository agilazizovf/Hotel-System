package com.project.hotel.controller;

import com.project.hotel.service.ContentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('SAVE_FILE')")
    public ResponseEntity<String> saveFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder) {
        try {
            String filePath = contentService.saveFile(file, folder);
            return ResponseEntity.ok("File saved successfully at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error saving file: " + e.getMessage());
        }
    }

    @PostMapping("/savePicture/{hotelId}")
    @PreAuthorize("hasAuthority('SAVE_PICTURE')")
    public ResponseEntity<String> saveHotelPicture(@RequestParam("file") MultipartFile file,
                                              @PathVariable Long hotelId) {
        try {
            contentService.saveHotelPicture(file, hotelId);
            return ResponseEntity.ok("Picture uploaded and saved successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error uploading picture: " + e.getMessage());
        }
    }

    @PutMapping("/pictures/{id}/{hotelId}")
    @PreAuthorize("hasAuthority('UPDATE_PICTURE')")
    public ResponseEntity<String> updateHotelPicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @PathVariable Long hotelId) {
        try {
            contentService.updateHotelPicture(file, id, hotelId);
            return ResponseEntity.ok("Picture updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error updating picture: " + e.getMessage());
        }
    }

    @PostMapping("/savePicture/{roomId}")
    @PreAuthorize("hasAuthority('SAVE_PICTURE')")
    public ResponseEntity<String> saveRoomPicture(@RequestParam("file") MultipartFile file,
                                              @PathVariable Long roomId) {
        try {
            contentService.saveRoomPicture(file, roomId);
            return ResponseEntity.ok("Picture uploaded and saved successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error uploading picture: " + e.getMessage());
        }
    }

    @PutMapping("/pictures/{id}/{roomId}")
    @PreAuthorize("hasAuthority('UPDATE_PICTURE')")
    public ResponseEntity<String> updateRoomPicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @PathVariable Long roomId) {
        try {
            contentService.updateRoomPicture(file, id, roomId);
            return ResponseEntity.ok("Picture updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error updating picture: " + e.getMessage());
        }
    }
}

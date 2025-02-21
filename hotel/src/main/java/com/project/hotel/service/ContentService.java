package com.project.hotel.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContentService {

    String saveFile(MultipartFile file, String folder)throws IOException;

    void saveHotelPicture(MultipartFile file, Long hotelId) throws IOException;
    void updateHotelPicture(MultipartFile file, Long id, Long hotelId) throws IOException;

    void saveRoomPicture(MultipartFile file, Long roomId) throws IOException;
    void updateRoomPicture(MultipartFile file, Long id, Long roomId) throws IOException;
}

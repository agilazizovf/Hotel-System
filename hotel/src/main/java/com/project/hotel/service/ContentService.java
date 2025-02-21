package com.project.hotel.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContentService {

    String saveFile(MultipartFile file, String folder)throws IOException;

    void savePicture(MultipartFile file, Long hotelId) throws IOException;
    void updatePicture(MultipartFile file, Long id, Long hotelId) throws IOException;
}

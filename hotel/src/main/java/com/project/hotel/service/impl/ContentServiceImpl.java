package com.project.hotel.service.impl;

import com.project.hotel.entity.HotelEntity;
import com.project.hotel.entity.PictureContentEntity;
import com.project.hotel.exception.CustomException;
import com.project.hotel.repository.HotelRepository;
import com.project.hotel.repository.PictureContentRepository;
import com.project.hotel.service.ContentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final PictureContentRepository pictureContentRepository;
    private final HotelRepository hotelRepository;
    @Override
    public String saveFile(MultipartFile file, String folder) throws IOException {
        // Define the base upload directory (this will be dynamically based on the folder parameter)
        String uploadDir = "uploads/" + folder;

        // Create the folder if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Ensure all directories in the path are created
        }

        // Get the original filename and extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new CustomException("Yanlış fayl adı","Invalid file name", "Bad Request", 400, null);
        }
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // Generate a random name for the file to avoid conflicts
        String randomName = UUID.randomUUID().toString();
        String randomFileName = randomName + fileExtension;

        // Save the file to the disk in the specified folder
        Path filePath = Paths.get(uploadDir, randomFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative file path (path relative to the base directory)
        return filePath.toString();
    }

    @Override
    public void savePicture(MultipartFile file, Long hotelId) throws IOException {
        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        // Save the file
        String filePath = saveFile(file, "hotels");

        // Create picture entity and associate it with the hotel
        PictureContentEntity pictureContent = new PictureContentEntity();
        pictureContent.setImagePath(filePath);

        // Add to hotel's image list
        hotel.getImages().add(pictureContent);
        pictureContentRepository.save(pictureContent);
        hotelRepository.save(hotel);
    }

    @Override
    public void updatePicture(MultipartFile file, Long id, Long hotelId) throws IOException {
        // Find the existing picture content by ID
        PictureContentEntity content = pictureContentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Picture content not found"));

        // Check if the picture belongs to the given hotel
        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        if (!hotel.getImages().contains(content)) {
            throw new CustomException("Invalid request", "Picture does not belong to the specified hotel", "Bad Request", 400, null);
        }

        // Delete the old picture if it exists
        if (content.getImagePath() != null) {
            Path oldFilePath = Paths.get(content.getImagePath());
            try {
                Files.deleteIfExists(oldFilePath);  // Delete the old file
            } catch (IOException e) {
                throw new IOException("Error deleting old picture file: " + e.getMessage());
            }
        }

        // Save the new picture file and generate its path
        String filePath = saveFile(file, "hotels");

        // Update the image path in the database
        content.setImagePath(filePath);
        pictureContentRepository.save(content);
    }

}

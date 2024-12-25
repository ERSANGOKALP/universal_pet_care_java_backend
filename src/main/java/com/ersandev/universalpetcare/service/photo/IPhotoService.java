package com.ersandev.universalpetcare.service.photo;

import com.ersandev.universalpetcare.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface IPhotoService {
    Photo savePhoto(MultipartFile file,Long userId) throws IOException, SQLException;
    Photo getPhotoById(Long id);
    void deletePhoto(Long id,Long userId);
    Photo updatePhoto(Long id, MultipartFile file) throws IOException, SQLException;
    byte[] getImageData(Long id) throws SQLException;
}

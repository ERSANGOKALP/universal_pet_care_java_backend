package com.ersandev.universalpetcare.controller;

import com.ersandev.universalpetcare.exception.ResourceNotFoundException;
import com.ersandev.universalpetcare.model.Photo;
import com.ersandev.universalpetcare.response.ApiResponse;
import com.ersandev.universalpetcare.service.photo.IPhotoService;
import com.ersandev.universalpetcare.utils.FeedBackMessage;
import com.ersandev.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(UrlMapping.PHOTOS)
@RequiredArgsConstructor
public class PhotoController {

    private final IPhotoService photoService;

    @PostMapping(UrlMapping.UPLOAD_PHOTO)
    public ResponseEntity<ApiResponse> savePhoto(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("userId") Long userId) throws SQLException, IOException {

        try {
            Photo thePhoto = photoService.savePhoto(file, userId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.CREATE_SUCCESS,thePhoto.getId()));
        } catch (IOException | SQLException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping(UrlMapping.GET_PHOTO_BY_ID)
    public ResponseEntity<ApiResponse> getPhotoById(@PathVariable Long photoId){

        try {
            Photo photo = photoService.getPhotoById(photoId);
            if (photo != null){
                byte[] photoBytes = photoService.getImageData(photo.getId());
                return ResponseEntity.ok(new ApiResponse(FeedBackMessage.RESOURCE_FOUND,photoBytes));
            }
        } catch (ResourceNotFoundException | SQLException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(null,INTERNAL_SERVER_ERROR));
    }


    @PutMapping(UrlMapping.UPDATE_PHOTO)
    public ResponseEntity<ApiResponse> updatePhoto(@PathVariable Long photoId, @RequestBody MultipartFile file) throws SQLException, IOException {

        try {
            Photo photo = photoService.getPhotoById(photoId);
            if (photo != null) {
                Photo updatedPhoto = photoService.updatePhoto(photo.getId(), file);
                return ResponseEntity.ok(new ApiResponse(FeedBackMessage.UPDATE_SUCCESS,updatedPhoto.getId()));
            }
        } catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(FeedBackMessage.NOT_FOUND,null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(null,INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping(UrlMapping.DELETE_PHOTO)
    public ResponseEntity<ApiResponse> deletePhoto(@PathVariable Long photoId, @PathVariable Long userId){

        try {
            Photo photo = photoService.getPhotoById(photoId);
            if (photo != null){
                photoService.deletePhoto(photo.getId(),userId);
                return ResponseEntity.ok(new ApiResponse(FeedBackMessage.DELETE_SUCCESS,photo.getId()));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(null, INTERNAL_SERVER_ERROR));
    }



}

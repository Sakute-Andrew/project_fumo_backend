package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.service.PhotoService;
import com.sakute.project_fumo_backend.domain.service.impl.PhotoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/photo")
public class PhotoController {

    private PhotoServiceImpl photoService;

    @Autowired
    public PhotoController(PhotoServiceImpl photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("file")MultipartFile file) throws IOException {
        String uploadImage = photoService.uploadPostPhoto(file, file.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData = photoService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }
}

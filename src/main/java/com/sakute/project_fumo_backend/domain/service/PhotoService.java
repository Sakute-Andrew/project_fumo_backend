package com.sakute.project_fumo_backend.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PhotoService {

     String uploadUserPhoto(MultipartFile file, UUID userId) throws IOException;
     String uploadPostPhoto(MultipartFile file, UUID postId) throws IOException;
     byte[] downloadImageFromFileSystem(String fileName) throws IOException;
}

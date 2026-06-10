package com.sakute.project_fumo_backend.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String upload(MultipartFile file) throws IOException;
    byte[] download(String subfolder, String fileName) throws IOException;
}

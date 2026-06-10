package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.service.FileService;
import com.sakute.project_fumo_backend.controller.exception.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final Set<String> IMAGE_EXT = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final Set<String> DOC_EXT = Set.of("pdf", "docx", "xlsx", "txt");

    @Value("${file.storage.path:data/files/}")
    private String storagePath;

    public String upload(MultipartFile file) throws IOException {
        String original = file.getOriginalFilename();
        String ext = getExtension(original);

        String subfolder;
        if (IMAGE_EXT.contains(ext)) {
            subfolder = "photos";
        } else if (DOC_EXT.contains(ext)) {
            subfolder = "documents";
        } else {
            throw new InvalidInputException("Непідтримуваний тип файлу: " + ext);
        }

        String fileName = UUID.randomUUID() + "." + ext;
        Path path = Paths.get(storagePath, subfolder, fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        return "/api/v1/files/" + subfolder + "/" + fileName;
    }

    public byte[] download(String subfolder, String fileName) throws IOException {
        Path path = Paths.get(storagePath, subfolder, fileName);
        if (!Files.exists(path)) throw new NotFoundException("Файл не знайдено: " + fileName);
        return Files.readAllBytes(path);
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot + 1).toLowerCase() : "";
    }
}
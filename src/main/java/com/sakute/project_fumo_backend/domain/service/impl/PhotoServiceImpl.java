package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.enteties.Photo;
import com.sakute.project_fumo_backend.domain.service.PhotoService;
import com.sakute.project_fumo_backend.repository.jpa_repo.PhotoRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Value("${data.photo.post}")
    private String postPhotoPath;

    @Value("${data.photo.user}")
    private String userPhotoPath;

    @Override
    public String uploadUserPhoto(MultipartFile file, UUID userId) throws IOException {
        String userDir = userPhotoPath;
        return uploadFile(file, userDir);
    }

    @Override
    public String uploadPostPhoto(MultipartFile file, String filename) throws IOException {
        String postDir = postPhotoPath;
        return uploadFile(file, postDir);
    }


    private String uploadFile(@NotNull MultipartFile file, String directory) throws IOException {
        // Створюємо директорію, якщо її нема
        Path dirPath = Paths.get(directory);
        Files.createDirectories(dirPath);

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String filename = UUID.randomUUID() + extension;

        Path targetPath = dirPath.resolve(filename);
        file.transferTo(targetPath);

        // Зберігаємо дані про фото у базу
        Photo photo = Photo.builder()
                .name(filename)
                .type(file.getContentType())
                .photoFilePath(targetPath.toString())
                .build();

        photoRepository.save(photo);

        return targetPath.toString();
    }


    @Override
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<Photo> fileData = photoRepository.findByName(fileName);
        String filePath = fileData.get().getPhotoFilePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }
}

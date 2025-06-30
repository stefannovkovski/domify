package com.example.domify.service.impl;
import com.example.domify.model.Property;
import com.example.domify.model.PropertyImage;
import com.example.domify.model.Unit;
import com.example.domify.model.UnitImage;
import com.example.domify.repository.UnitImageRepository;
import com.example.domify.repository.UnitRepository;
import com.example.domify.service.UnitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;
    private final UnitImageRepository unitImageRepository;

    @Value("${app.upload.dir:static/uploads/properties/}")
    private String uploadDir;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    public UnitServiceImpl(UnitRepository unitRepository, UnitImageRepository unitImageRepository) {
        this.unitRepository = unitRepository;
        this.unitImageRepository = unitImageRepository;
    }
    @Override
    public Optional<Unit> findDetails(Long unitDetails) {
        return unitRepository.findById(unitDetails);
    }

    @Override
    public Unit save(Unit unit, MultipartFile[] images) throws IOException {
        Unit savedUnit = unitRepository.save(unit);
        if (images != null && images.length > 0) {
            processImageUploads(images, unit);
        }

        return savedUnit;
    }

    @Override
    public Unit findById(Long id) {
        return unitRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    private void processImageUploads(MultipartFile[] images, Unit unit) throws IOException {
        String uploadDir = "uploads/units/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (int i = 0; i < images.length; i++) {
            MultipartFile image = images[i];
            if (image.isEmpty()) {
                continue;
            }
            validateImageFile(image);
            String originalFilename = image.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String filename = String.format("property_%d_img_%03d.%s",
                    unit.getId(), i + 1, extension);

            Path destinationPath = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), destinationPath);

            String imagePath = "/uploads/units/" + filename;
            UnitImage propertyImage = new UnitImage(imagePath, unit);
            unitImageRepository.save(propertyImage);
        }

    }

    private void validateImageFile(MultipartFile file) throws IOException {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds maximum allowed size of 5MB");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            throw new IOException("Invalid filename");
        }

        String extension = getFileExtension(filename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IOException("File type not allowed. Allowed types: " + ALLOWED_EXTENSIONS);
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("File is not a valid image");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot + 1);
    }

}

package com.example.domify.service.impl;

import com.example.domify.model.Address;
import com.example.domify.model.Property;
import com.example.domify.model.PropertyImage;
import com.example.domify.model.UserD;
import com.example.domify.repository.AddressRepository;
import com.example.domify.repository.PropertyImageRepository;
import com.example.domify.repository.PropertyRepository;
import com.example.domify.repository.PropertyTypeRepository;
import com.example.domify.service.PropertiesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PropertiesServiceImpl implements PropertiesService {
    private final PropertyRepository propertyRepository;
    private final PropertyTypeRepository propertyTypeRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final AddressRepository addressRepository;

    @Value("${app.upload.dir:static/uploads/properties/}")
    private String uploadDir;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public PropertiesServiceImpl(PropertyRepository propertyRepository,
                                 PropertyTypeRepository propertyTypeRepository,
                                 PropertyImageRepository propertyImageRepository,
                                 AddressRepository addressRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyTypeRepository = propertyTypeRepository;
        this.propertyImageRepository = propertyImageRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Property> findByOwnerId(Long userId) {
        return propertyRepository.findAllByOwnerId(userId);
    }

    @Override
    public Optional<Property> findDetails(Long propertyDetails) {
        return propertyRepository.findById(propertyDetails);
    }

    @Override
    public Property createPropertyWithImages(Long propertyTypeId, String name, String description,
                                             String street, String streetNumber, String municipality,
                                             String city, String country, MultipartFile[] images,
                                             UserD owner) throws IOException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Property name cannot be empty");
        }

        var propertyType = propertyTypeRepository.findById(propertyTypeId)
                .orElseThrow(() -> new RuntimeException("Property type not found with ID: " + propertyTypeId));

        Address address = new Address(street, streetNumber, municipality, city, country);
        address = addressRepository.save(address);

        Property property = new Property(name, description, LocalDateTime.now(), owner, propertyType, address);
        property = propertyRepository.save(property);

        if (images != null && images.length > 0) {
            processImageUploads(images, property);
        }
        return property;
    }

    private void processImageUploads(MultipartFile[] images, Property property) throws IOException {
        String uploadDir = "uploads/properties/";
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
                    property.getId(), i + 1, extension);

            Path destinationPath = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), destinationPath);

            String imagePath = "/uploads/properties/" + filename;
            PropertyImage propertyImage = new PropertyImage(imagePath, property);
            propertyImageRepository.save(propertyImage);
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
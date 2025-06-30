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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PropertiesServiceImpl implements PropertiesService {
    private final PropertyRepository propertyRepository;
    private final PropertyTypeRepository propertyTypeRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final AddressRepository addressRepository;

    public PropertiesServiceImpl(PropertyRepository propertyRepository, PropertyTypeRepository propertyTypeRepository, PropertyImageRepository propertyImageRepository, AddressRepository addressRepository) {
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

        var propertyType = propertyTypeRepository.findById(propertyTypeId)
                .orElseThrow(() -> new RuntimeException("Property type not found with ID: " + propertyTypeId));

        Address address = new Address(street, streetNumber, municipality, city, country);
        addressRepository.save(address);
        Property property = new Property(name, description, LocalDateTime.now(), owner,
                propertyType, address);
        property = propertyRepository.save(property);
        if (images != null && images.length > 0) {
            String uploadBaseDir = "src/main/resources/static/uploads/properties/";
            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                if (!image.isEmpty()) {
                    String filename = String.format("property_%d_img_%03d.jpg", property.getId(), i + 1);

                    File uploadDir = new File(uploadBaseDir);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    File destinationFile = new File(uploadDir, filename);
                    image.transferTo(destinationFile);

                    String imagePath = "/uploads/properties/" + filename;
                    PropertyImage propertyImage = new PropertyImage(imagePath, property);
                    propertyImageRepository.save(propertyImage);
                }
            }
        }

        return property;
    }
}
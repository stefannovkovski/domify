package com.example.domify.service;

import com.example.domify.model.Property;
import com.example.domify.model.UserD;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PropertiesService {

    List<Property> findByOwnerId(Long userId);
    Optional<Property> findDetails(Long propertyDetails);

    Property createPropertyWithImages(Long propertyTypeId, String name, String description,
                                      String street, String streetNumber, String municipality,
                                      String city, String country, MultipartFile[] images,
                                      UserD owner) throws IOException;
    Property findById(Long id);
}

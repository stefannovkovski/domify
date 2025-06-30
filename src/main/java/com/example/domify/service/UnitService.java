package com.example.domify.service;

import com.example.domify.model.Unit;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UnitService {
    Optional<Unit> findDetails(Long unitDetails);
    Unit save(Unit unit, MultipartFile[] images) throws IOException;
    Unit findById(Long id);
}

package com.example.domify.service.impl;
import com.example.domify.model.Unit;
import com.example.domify.repository.UnitRepository;
import com.example.domify.service.UnitService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;
    public UnitServiceImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }
    @Override
    public Optional<Unit> findDetails(Long unitDetails) {
        return unitRepository.findById(unitDetails);
    }

}

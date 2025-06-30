package com.example.domify.service;

import com.example.domify.model.Unit;

import java.util.Optional;

public interface UnitService {

    Optional<Unit> findDetails(Long unitDetails);

}

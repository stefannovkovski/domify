package com.example.domify.service.impl;

import com.example.domify.model.Address;
import com.example.domify.repository.AddressRepository;
import com.example.domify.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressReposiotry;

    public AddressServiceImpl(AddressRepository addressReposiotry) {
        this.addressReposiotry = addressReposiotry;
    }

    @Override
    public List<Address> findAll() {
        return addressReposiotry.findAll();
    }
}

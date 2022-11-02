package com.github.av.bytelegrambot.service;


import com.github.av.bytelegrambot.repository.BrandRepository;
import com.github.av.bytelegrambot.repository.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService{

    final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public Optional<Brand> getByName(String name) {

        return brandRepository.findByNameIs(name);
    }
}

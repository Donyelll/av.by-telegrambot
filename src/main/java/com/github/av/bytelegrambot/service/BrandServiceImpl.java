package com.github.av.bytelegrambot.service;


import com.github.av.bytelegrambot.repository.BrandRepository;
import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService{

    final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public void save(BrandEntity brandEntity) {
        brandRepository.save(brandEntity);
    }

    @Override
    public void saveAll(List<BrandEntity> brandEntities) {
        brandRepository.saveAll(brandEntities);
    }

    @Override
    public Optional<BrandEntity> getByName(String name) {

        return brandRepository.findFirstByNameIgnoreCase(name);
    }

    @Override
    public Optional<BrandEntity> getById(int id) {
        return brandRepository.findById(id);
    }

    @Override
    public List<BrandEntity> getAllBrands() {
        return brandRepository.findAllByIdNotNull();
    }
}

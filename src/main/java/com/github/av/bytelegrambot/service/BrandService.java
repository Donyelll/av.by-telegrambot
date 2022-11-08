package com.github.av.bytelegrambot.service;

import com.github.av.bytelegrambot.repository.entity.BrandEntity;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    void save(BrandEntity brandEntity);

    void saveAll(List<BrandEntity> brandEntities);

    void deleteAll();

    Optional<BrandEntity> getByName(String name);

    List<BrandEntity> getAllBrands();

    Optional<BrandEntity> getById(int id);


}

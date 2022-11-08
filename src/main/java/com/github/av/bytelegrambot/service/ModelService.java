package com.github.av.bytelegrambot.service;

import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;


import java.util.List;
import java.util.Optional;

public interface ModelService {

    void save(ModelEntity modelEntity);

    void saveAll(List<ModelEntity> modelEntities);

    Optional<ModelEntity> getByName(String name);

    List<ModelEntity> getAllByBrand(int id);

    List<ModelEntity> getAllModels();
}

package com.github.av.bytelegrambot.service;

import com.github.av.bytelegrambot.repository.entity.GenerationEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;

import java.util.List;
import java.util.Optional;

public interface GenerationService {

    void save(GenerationEntity generationEntity);

    void saveAll(List<GenerationEntity> generationEntities);

    void deleteAll();

    Optional<GenerationEntity> getByName(String name);

    List<GenerationEntity> getAllByModel(int id);

    List<GenerationEntity> getAllGenerations();

}

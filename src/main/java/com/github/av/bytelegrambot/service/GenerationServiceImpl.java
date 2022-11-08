package com.github.av.bytelegrambot.service;


import com.github.av.bytelegrambot.repository.GenerationRepository;
import com.github.av.bytelegrambot.repository.entity.GenerationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenerationServiceImpl implements GenerationService{

    final GenerationRepository generationRepository;

    @Override
    public void saveAll(List<GenerationEntity> generationEntities) {
        generationRepository.saveAll(generationEntities);
    }

    @Autowired
    public GenerationServiceImpl(GenerationRepository generationRepository) {
        this.generationRepository = generationRepository;
    }

    @Override
    public void save(GenerationEntity generationEntity) {
        generationRepository.save(generationEntity);
    }

    @Override
    public List<GenerationEntity> getAllByModel(int id) {
        return generationRepository.findAllByModel_id(id);
    }

    @Override
    public List<GenerationEntity> getAllGenerations() {
        return generationRepository.findAllByIdNotNull();
    }

    @Override
    public Optional<GenerationEntity> getByName(String name) {

        return generationRepository.findFirstByNameIgnoreCase(name);
    }
}

package com.github.av.bytelegrambot.service;


import com.github.av.bytelegrambot.repository.BrandRepository;
import com.github.av.bytelegrambot.repository.GenerationRepository;
import com.github.av.bytelegrambot.repository.entity.Brand;
import com.github.av.bytelegrambot.repository.entity.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenerationServiceImpl implements GenerationService{

    final GenerationRepository generationRepository;

    @Autowired
    public GenerationServiceImpl(GenerationRepository generationRepository) {
        this.generationRepository = generationRepository;
    }

    @Override
    public void save(Generation generation) {
        generationRepository.save(generation);
    }

    @Override
    public List<Generation> getAllByModel(int id) {
        return generationRepository.findAllByModel_id(id);
    }

    @Override
    public Optional<Generation> getByName(String name) {

        return generationRepository.findByNameIs(name);
    }
}

package com.github.av.bytelegrambot.service;


import com.github.av.bytelegrambot.repository.ModelRepository;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelServiceImpl implements ModelService{

    final ModelRepository modelRepository;

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Override
    public void save(ModelEntity modelEntity) {
        modelRepository.save(modelEntity);
    }

    @Override
    public void deleteAll() {
       modelRepository.deleteAll();
    }

    @Override
    public List<ModelEntity> getAllByBrand(int id) {
        return modelRepository.findAllByBrand_id(id);
    }

    @Override
    public Optional<ModelEntity> getByName(String name) {

        return modelRepository.findFirstByNameIgnoreCase(name);
    }

    @Override
    public Optional<ModelEntity> getById(int id) {
        return modelRepository.findById(id);
    }

    @Override
    public void saveAll(List<ModelEntity> modelEntities) {
        modelRepository.saveAll(modelEntities);
    }

    @Override
    public List<ModelEntity> getAllModels() {
        return modelRepository.findAllByIdNotNull();
    }
}

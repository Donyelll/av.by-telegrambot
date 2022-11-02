package com.github.av.bytelegrambot.service;


import com.github.av.bytelegrambot.repository.BrandRepository;
import com.github.av.bytelegrambot.repository.ModelRepository;
import com.github.av.bytelegrambot.repository.entity.Brand;
import com.github.av.bytelegrambot.repository.entity.Model;
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
    public void save(Model model) {
        modelRepository.save(model);
    }

    @Override
    public List<Model> getAllByBrand(int id) {
        return modelRepository.findAllByBrand_id(id);
    }

    @Override
    public Optional<Model> getByName(String name) {

        return modelRepository.findByNameIs(name);
    }
}

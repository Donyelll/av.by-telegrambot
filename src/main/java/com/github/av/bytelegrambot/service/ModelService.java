package com.github.av.bytelegrambot.service;

import com.github.av.bytelegrambot.repository.entity.Brand;
import com.github.av.bytelegrambot.repository.entity.Model;

import java.util.List;
import java.util.Optional;

public interface ModelService {

    void save(Model model);

    Optional<Model> getByName(String name);

    List<Model> getAllByBrand(int id);

}

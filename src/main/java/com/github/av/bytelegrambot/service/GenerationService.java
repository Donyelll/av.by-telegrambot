package com.github.av.bytelegrambot.service;

import com.github.av.bytelegrambot.repository.entity.Brand;
import com.github.av.bytelegrambot.repository.entity.Generation;
import com.github.av.bytelegrambot.repository.entity.Model;

import java.util.List;
import java.util.Optional;

public interface GenerationService {

    void save(Generation generation);

    Optional<Generation> getByName(String name);

    List<Generation> getAllByModel(int id);

}

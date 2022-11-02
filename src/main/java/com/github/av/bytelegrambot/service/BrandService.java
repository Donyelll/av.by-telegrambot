package com.github.av.bytelegrambot.service;

import com.github.av.bytelegrambot.repository.entity.Brand;

import java.util.Optional;

public interface BrandService {

    void save(Brand brand);

    Optional<Brand> getByName(String name);
}

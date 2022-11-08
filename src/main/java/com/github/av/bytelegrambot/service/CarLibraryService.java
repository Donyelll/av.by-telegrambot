package com.github.av.bytelegrambot.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class CarLibraryService  {

    private BrandServiceImpl brandService;

    private ModelServiceImpl modelService;

    private GenerationServiceImpl generationService;

    @Autowired
    public CarLibraryService(BrandServiceImpl brandService, ModelServiceImpl modelService, GenerationServiceImpl generationService) {
        this.brandService = brandService;
        this.modelService = modelService;
        this.generationService = generationService;
    }


}

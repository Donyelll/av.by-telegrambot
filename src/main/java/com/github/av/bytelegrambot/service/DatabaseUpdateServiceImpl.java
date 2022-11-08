package com.github.av.bytelegrambot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import com.github.av.bytelegrambot.repository.entity.GenerationEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class DatabaseUpdateServiceImpl implements DatabaseUpdateService {

    private final String BRANDS_PATH = "https://api.av.by/offer-types/cars/catalog/brand-items";
    private final String MODELS_PATH = "https://api.av.by/offer-types/cars/catalog/brand-items/%d/models";
    private final String GENERATIONS_PATH = "https://api.av.by/offer-types/cars/catalog/brand-items/%d/models/%d/generations";
    private String requestPath;
    private final CarLibraryService carLibraryService;

    @Autowired
    public DatabaseUpdateServiceImpl(CarLibraryService carLibraryService) {
        this.carLibraryService = carLibraryService;
    }

    @Override
    public void updateDB() {

        getCarLibraryService().getGenerationService().deleteAll();
        getCarLibraryService().getModelService().deleteAll();
        getCarLibraryService().getBrandService().deleteAll();

        initDB();
    }

    @Override
    public void initDB() {
        final RestTemplate restTemplate = new RestTemplate();
        setRequestPath(BRANDS_PATH);

        List<BrandEntity> brands = new ArrayList<>();
        List<GenerationEntity> generations = new ArrayList<>();
        List<GenerationEntity> generationsOfEachModel = new ArrayList<>();
        List<ModelEntity> modelsOfEachBrand = new ArrayList<>();
        List<ModelEntity> models = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            brands.addAll(mapper.readValue(restTemplate.getForObject(requestPath, String.class), new TypeReference<List<BrandEntity>>(){}));

            for (BrandEntity brand: brands) {
                setRequestPath(String.format(MODELS_PATH,brand.getId()));

                modelsOfEachBrand.addAll(mapper.readValue(restTemplate.getForObject(requestPath, String.class), new TypeReference<List<ModelEntity>>(){}));

                for (ModelEntity model: modelsOfEachBrand) {
                    model.setBrand(brand);
                    models.add(model);

                    setRequestPath(String.format(GENERATIONS_PATH, brand.getId(), model.getId()));
                    generationsOfEachModel.addAll(mapper.readValue(restTemplate.getForObject(requestPath, String.class), new TypeReference<List<GenerationEntity>>(){}));

                    for (GenerationEntity generation: generationsOfEachModel) {
                        generation.setBrand(brand);
                        generation.setModel(model);
                        generations.add(generation);
                    }
                    generationsOfEachModel.clear();
                }
                modelsOfEachBrand.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        carLibraryService.getBrandService().saveAll(brands);
        carLibraryService.getModelService().saveAll(models);
        carLibraryService.getGenerationService().saveAll(generations);


    }


}

package com.github.av.bytelegrambot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import com.github.av.bytelegrambot.repository.entity.GenerationEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import com.github.av.bytelegrambot.service.CarLibraryService;
import com.github.av.bytelegrambot.service.LocalizationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@Getter
@Setter
public class AvbyApiClientImpl implements AvbyApiClient{


    private final String BASE_ADVERTISEMENTS_PATH = "https://cars.av.by/filter?";

                /*    %s = [brand, model, generation]   %d = id    */
    private final String ADDITIONAL_PARAMETERS_PATH = "&brands[0][%s]=%d";

    private final String NO_ADS_FOUND_MESSAGE_KEY = "no_ads_found_message_key";
    private String requestPath;

    private final LocalizationService localizationService;
    private final CarLibraryService carLibraryService;
    private String carArgs = "";
    private String state = "";

    @Autowired
    public AvbyApiClientImpl(LocalizationService localizationService, CarLibraryService carLibraryService) {
        this.localizationService = localizationService;
        this.carLibraryService = carLibraryService;
    }

    @Override
    public String getAllAds(int brandId, int modelId, int generationId) {

        setCarArgs("");
        final RestTemplate restTemplate = new RestTemplate();
        setRequestPath(BASE_ADVERTISEMENTS_PATH);
        StringBuilder builder = new StringBuilder(requestPath);
        if(generationId != 0){
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "brand", brandId));
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "model", modelId));
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "generation", generationId));
        }else if(modelId != 0 ){
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "brand", brandId));
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "model", modelId));
        }else{
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "brand", brandId));
        }
        final String stringPosts = restTemplate.getForObject(builder.toString(), String.class);
        Pattern pattern = Pattern.compile("(?<=\"publicUrl\":\")(.*?)(\\d+)(?=\",)");
        Matcher matcher = pattern.matcher(stringPosts);
        StringBuilder sb = new StringBuilder();
        if(matcher.find()) {
            do {
                sb.append(matcher.group(1)).append(matcher.group(2));
                sb.append("\n");

            } while (matcher.find());


            return sb.toString();
        }

        return localizationService.getMessage(NO_ADS_FOUND_MESSAGE_KEY);
    }


}

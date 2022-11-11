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
public class AvbyApiClientImpl implements AvbyApiClient {


    private final String BASE_ADVERTISEMENTS_PATH = "https://cars.av.by/filter?";

    /*    %s = [brand, model, generation]   %d = id    */
    private final String ADDITIONAL_PARAMETERS_PATH = "&brands[0][%s]=%d";
    private final String PAGE_PATH_PARAMETER = "&page=%d";
    private String requestPath;

    private final String NO_ADS_FOUND_MESSAGE_KEY = "no_ads_found_message_key";

    private final LocalizationService localizationService;
    private final CarLibraryService carLibraryService;

    private String carArgs = "";
    private String state = "";

    private List<String> adsList = new ArrayList<>();



    @Autowired
    public AvbyApiClientImpl(LocalizationService localizationService, CarLibraryService carLibraryService) {
        this.localizationService = localizationService;
        this.carLibraryService = carLibraryService;
    }

    @Override
    public void getAllAds(int brandId, int modelId, int generationId) {
        getAdsList().clear();
        setCarArgs("");
        final RestTemplate restTemplate = new RestTemplate();
        setRequestPath(BASE_ADVERTISEMENTS_PATH);
        StringBuilder builder = new StringBuilder(requestPath);
        if (generationId != 0) {
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "brand", brandId));
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "model", modelId));
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "generation", generationId));
        } else if (modelId != 0) {
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "brand", brandId));
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "model", modelId));
        } else {
            builder.append(String.format(ADDITIONAL_PARAMETERS_PATH, "brand", brandId));
        }
        String stringPosts = restTemplate.getForObject(builder.toString(), String.class);
        String adsLinksRegEx = "(?<=\"publicUrl\":\")(.*?)(\\d+)(?=\",)";
        String totalAdsCountRegEx = "(?<=<!-- --> <!-- -->)(\\d+)(?=<!-- --> <!-- -->)";
        Pattern pattern = Pattern.compile(totalAdsCountRegEx);
        Matcher matcher = pattern.matcher(stringPosts);
        StringBuilder sb = new StringBuilder();
        int adsCount = 0;
        int pageCount = 1;
        if (matcher.find()) {
            adsCount = Integer.parseInt(matcher.group(1));
            pageCount = (int) Math.ceil(adsCount / 25f);
        }
        pattern = Pattern.compile(adsLinksRegEx);
        matcher = pattern.matcher(stringPosts);
        int i = 1;
        if (pageCount == 1) {
            if (matcher.find()) {

                do {
                    sb.append(i++).append(". ");
                    sb.append(matcher.group(1)).append(matcher.group(2));
                    sb.append("\n");

                } while (matcher.find());

                adsList.add(sb.toString());
                return;
            }
        } else if (pageCount > 1) {
            builder.append(String.format(PAGE_PATH_PARAMETER, 1));
            for (int j = 1; j <= pageCount; j++) {

                stringPosts = restTemplate.getForObject(builder.toString(), String.class);
                matcher = pattern.matcher(stringPosts);

                if (matcher.find()) {
                    do {
                        sb.append(i++).append(". ");
                        sb.append(matcher.group(1)).append(matcher.group(2));
                        sb.append("\n");

                    } while (matcher.find());
                    int tmp = j+1;
                    builder.replace(builder.indexOf("&page=") ,builder.length(), String.format(PAGE_PATH_PARAMETER, tmp));

                }
                adsList.add(sb.toString());
                sb.delete(0, sb.length());
            }
            return;
        }

            adsList.add(localizationService.getMessage(NO_ADS_FOUND_MESSAGE_KEY));
        }
    }


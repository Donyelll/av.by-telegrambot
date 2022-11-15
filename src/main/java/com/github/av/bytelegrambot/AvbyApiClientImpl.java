package com.github.av.bytelegrambot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.av.bytelegrambot.chart.Chart;
import com.github.av.bytelegrambot.chart.ChartData;
import com.github.av.bytelegrambot.chart.ChartDatasets;
import com.github.av.bytelegrambot.dto.CarAdvert;
import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import com.github.av.bytelegrambot.repository.entity.GenerationEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import com.github.av.bytelegrambot.service.CarLibraryService;
import com.github.av.bytelegrambot.service.LocalizationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
@Getter
@Setter
public class AvbyApiClientImpl implements AvbyApiClient {


    private final String BASE_ADVERTISEMENTS_PATH = "https://cars.av.by/filter?";
    private final String BASE_CHART_PATH = "https://quickchart.io/chart?bkg=white&c=";

    /*                                       %d = >=7 digits ads id        */
    private final String CAR_AD_PATH = "https://api.av.by/offers/%d";

    /*                                %s = [brand, model, generation]   %d = id    */
    private final String ADDITIONAL_PARAMETERS_PATH = "&brands[0][%s]=%d";
    private final String PAGE_PATH_PARAMETER = "&page=%d";
    private String requestPath;

    private final String NO_ADS_FOUND_MESSAGE_KEY = "no_ads_found_message_key";
    private final String SUMMARY_CAR_CHARTS_MESSAGE_KEY = "summary_car_charts_message";
    private final String TRANSMISSION_CAR_CHARTS_MESSAGE_KEY = "transmission_car_charts_message";
    private final String ENGINE_TYPE_CAR_CHARTS_MESSAGE_KEY = "engine_type_car_charts_message";
    private final String MECHANICAL_TRANSMISSION_MESSAGE_KEY = "mechanical_transmission_message";
    private final String AUTOMATIC_TRANSMISSION_MESSAGE_KEY = "automatic_transmission_message";
    private final String GASOLINE_ENGINE_MESSAGE_KEY = "gasoline_engine_message";
    private final String DIESEL_FUEL_ENGINE_MESSAGE_KEY = "diesel_fuel_engine_message";
    private final String ELECTRIC_ENGINE_MESSAGE_KEY = "electric_engine_message";
    private final String HYBRID_ENGINE_MESSAGE_KEY = "hybrid_engine_message";
    private final String AVERAGE_PRICE_MESSAGE_KEY = "average_price_message";

    private final LocalizationService localizationService;
    private final CarLibraryService carLibraryService;

    private String carArgs = "";
    private String state = "";

    private List<String> adsList = new ArrayList<>();
    private List<CarAdvert> carAdverts = new ArrayList<>();


    private int totalAdsCount = 0;

    @Override
    public List<String> getChartPhotoByURL() {
        List<String> chartBaseURLs = new ArrayList<>();

        for (Chart chart: getCharts()) {
            String chartJson = convertChartToJSON(chart);

            chartBaseURLs.add(BASE_CHART_PATH + URLEncoder.encode(chartJson,StandardCharsets.UTF_8) + "&random=64");

        }


        return chartBaseURLs;
    }

    @Override
    public String getCarChartsMessage() {
        StringBuilder builder = new StringBuilder();

        int totalAveragePrice = Math.round((float)
                carAdverts.stream()
                        .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                        .average().getAsDouble());
        int totalMaxPrice = carAdverts.stream()
                .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                .max()
                .getAsInt();
        int totalMinPrice = carAdverts.stream()
                .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                .min()
                .getAsInt();

        int totalAverageMileage = Math.round((float)
                carAdverts.stream()
                        .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                        .average().getAsDouble());
        int totalMaxMileAge = carAdverts.stream()
                .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                .max()
                .getAsInt();
        int totalMinMileage = carAdverts.stream()
                .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                .min()
                .getAsInt();

        builder.append(String.format(localizationService.getMessage(SUMMARY_CAR_CHARTS_MESSAGE_KEY)
                ,totalAdsCount
                ,totalAveragePrice
                ,totalMaxPrice
                ,totalMinPrice
                ,totalAverageMileage
                ,totalMaxMileAge
                ,totalMinMileage));

        if (carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getTransmission().equals("механика"))){

            long adsCount = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("механика"))
                    .count();

            int averagePrice = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getTransmission().equals("механика"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble());
            int maxPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("механика"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .max()
                    .getAsInt();
            int minPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("механика"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .min()
                    .getAsInt();

            int averageMileage = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getTransmission().equals("механика"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                            .average().getAsDouble());
            int maxMileAge = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("механика"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .max()
                    .getAsInt();
            int minMileage = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("механика"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .min()
                    .getAsInt();

            builder.append(String.format(localizationService.getMessage(TRANSMISSION_CAR_CHARTS_MESSAGE_KEY)
                    ,localizationService.getMessage(MECHANICAL_TRANSMISSION_MESSAGE_KEY)
                    ,adsCount
                    ,averagePrice
                    ,maxPrice
                    ,minPrice
                    ,averageMileage
                    ,maxMileAge
                    ,minMileage));

        }

        if (carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getTransmission().equals("автомат"))){

            long adsCount = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("автомат"))
                    .count();

            int averagePrice = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getTransmission().equals("автомат"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble());
            int maxPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("автомат"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .max()
                    .getAsInt();
            int minPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("автомат"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .min()
                    .getAsInt();

            int averageMileage = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getTransmission().equals("автомат"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                            .average().getAsDouble());
            int maxMileAge = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("автомат"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .max()
                    .getAsInt();
            int minMileage = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getTransmission().equals("автомат"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .min()
                    .getAsInt();

            builder.append(String.format(localizationService.getMessage(TRANSMISSION_CAR_CHARTS_MESSAGE_KEY)
                    ,localizationService.getMessage(AUTOMATIC_TRANSMISSION_MESSAGE_KEY)
                    ,adsCount
                    ,averagePrice
                    ,maxPrice
                    ,minPrice
                    ,averageMileage
                    ,maxMileAge
                    ,minMileage));

        }

        if (carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getEngineType().equals("бензин"))){

            long adsCount = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                    .count();

            int averagePrice = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble());
            int maxPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .max()
                    .getAsInt();
            int minPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .min()
                    .getAsInt();

            int averageMileage = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                            .average().getAsDouble());
            int maxMileAge = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .max()
                    .getAsInt();
            int minMileage = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .min()
                    .getAsInt();

            builder.append(String.format(localizationService.getMessage(ENGINE_TYPE_CAR_CHARTS_MESSAGE_KEY)
                    ,localizationService.getMessage(GASOLINE_ENGINE_MESSAGE_KEY)
                    ,adsCount
                    ,averagePrice
                    ,maxPrice
                    ,minPrice
                    ,averageMileage
                    ,maxMileAge
                    ,minMileage));

        }
        if (carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getEngineType().equals("дизель"))){

            long adsCount = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                    .count();

            int averagePrice = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble());
            int maxPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .max()
                    .getAsInt();
            int minPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .min()
                    .getAsInt();

            int averageMileage = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                            .average().getAsDouble());
            int maxMileAge = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .max()
                    .getAsInt();
            int minMileage = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .min()
                    .getAsInt();

            builder.append(String.format(localizationService.getMessage(ENGINE_TYPE_CAR_CHARTS_MESSAGE_KEY)
                    ,localizationService.getMessage(DIESEL_FUEL_ENGINE_MESSAGE_KEY)
                    ,adsCount
                    ,averagePrice
                    ,maxPrice
                    ,minPrice
                    ,averageMileage
                    ,maxMileAge
                    ,minMileage));

        }
        if (carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getEngineType().equals("электро"))){

            long adsCount = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                    .count();

            int averagePrice = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble());
            int maxPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .max()
                    .getAsInt();
            int minPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .min()
                    .getAsInt();

            int averageMileage = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                            .average().getAsDouble());
            int maxMileAge = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .max()
                    .getAsInt();
            int minMileage = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .min()
                    .getAsInt();

            builder.append(String.format(localizationService.getMessage(ENGINE_TYPE_CAR_CHARTS_MESSAGE_KEY)
                    ,localizationService.getMessage(ELECTRIC_ENGINE_MESSAGE_KEY)
                    ,adsCount
                    ,averagePrice
                    ,maxPrice
                    ,minPrice
                    ,averageMileage
                    ,maxMileAge
                    ,minMileage));

        }
        if (carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))){

            long adsCount = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                    .count();

            int averagePrice = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble());
            int maxPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .max()
                    .getAsInt();
            int minPrice = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                    .min()
                    .getAsInt();

            int averageMileage = Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                            .average().getAsDouble());
            int maxMileAge = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .max()
                    .getAsInt();
            int minMileage = carAdverts.stream()
                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getMileage()))
                    .min()
                    .getAsInt();

            builder.append(String.format(localizationService.getMessage(ENGINE_TYPE_CAR_CHARTS_MESSAGE_KEY)
                    ,localizationService.getMessage(HYBRID_ENGINE_MESSAGE_KEY)
                    ,adsCount
                    ,averagePrice
                    ,maxPrice
                    ,minPrice
                    ,averageMileage
                    ,maxMileAge
                    ,minMileage));

        }

        return builder.toString();
    }

    @Override
    public String convertChartToJSON(Chart chart) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String chartJSON = objectMapper.writeValueAsString(chart);
            return chartJSON;
        }catch (JsonProcessingException e ){
            e.printStackTrace();
        }
        return "blabla";
    }

    @Override
    public Chart getTransmissionPriceChart() {
        Chart priceChart = new Chart();
        ChartData chartData = new ChartData();
        ChartDatasets chartDatasetMechanical = new ChartDatasets();
        ChartDatasets chartDatasetAutomatic = new ChartDatasets();
        ChartDatasets chartDatasetAverage = new ChartDatasets();

        if(carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getTransmission().equals("механика"))) {
            chartDatasetMechanical.setType("bar");
            chartDatasetMechanical.setBackgroundColor("rgba(152, 156, 237, 0.8)");
            chartDatasetMechanical.setLabel("Механика");
            chartDatasetMechanical.setOrder(2);
            chartDatasetMechanical.getData().add(0);
            chartDatasetMechanical.getData().add(Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getTransmission().equals("механика"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble()));
            chartData.getDatasets().add(chartDatasetMechanical);
        }

        if(carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getTransmission().equals("автомат"))) {
            chartDatasetAutomatic.setType("bar");
            chartDatasetAutomatic.setBackgroundColor("rgba(230, 203, 79, 0.8)");
            chartDatasetAutomatic.setLabel("Автомат");
            chartDatasetAutomatic.setOrder(2);
            chartDatasetAutomatic.getData().add(0);
            chartDatasetAutomatic.getData().add(Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getTransmission().equals("автомат"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble()));
            chartData.getDatasets().add(chartDatasetAutomatic);
        }

        chartDatasetAverage.setType("line");
        chartDatasetAverage.setBackgroundColor("rgba(223, 60, 34, 0.8)");

        chartDatasetAverage.setLabel(localizationService.getMessage(AVERAGE_PRICE_MESSAGE_KEY));
        chartDatasetAverage.setOrder(1);
        int averagePrice = Math.round((float)
                carAdverts.stream()
                        .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                        .average().getAsDouble());
        chartDatasetAverage.getData().add(averagePrice);
        chartDatasetAverage.getData().add(averagePrice);
        chartDatasetAverage.getData().add(averagePrice);
        chartData.getDatasets().add(chartDatasetAverage);

        chartData.setLabels((Arrays.asList("0","Цены","3")));


        priceChart.setType("line");
        priceChart.setData(chartData);
        return priceChart;
    }

    @Override
    public Chart getEngineTypePriceChart() {
        Chart priceChart = new Chart();
        ChartData chartData = new ChartData();
        ChartDatasets chartDatasetGasoline = new ChartDatasets();
        ChartDatasets chartDatasetDiesel = new ChartDatasets();
        ChartDatasets chartDatasetElectric = new ChartDatasets();
        ChartDatasets chartDatasetHybrid = new ChartDatasets();
        ChartDatasets chartDatasetAverage = new ChartDatasets();

        if(carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getEngineType().equals("бензин"))) {
            chartDatasetGasoline.setType("bar");
            chartDatasetGasoline.setBackgroundColor("rgba(152, 156, 237, 0.8)");
            chartDatasetGasoline.setLabel("бензин");
            chartDatasetGasoline.setOrder(2);
            chartDatasetGasoline.getData().add(0);
            chartDatasetGasoline.getData().add(Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble()));
            chartData.getDatasets().add(chartDatasetGasoline);
        }

        if(carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getEngineType().equals("дизель"))) {
            chartDatasetDiesel.setType("bar");
            chartDatasetDiesel.setBackgroundColor("rgba(230, 203, 79, 0.8)");
            chartDatasetDiesel.setLabel("дизель");
            chartDatasetDiesel.setOrder(2);
            chartDatasetDiesel.getData().add(0);
            chartDatasetDiesel.getData().add(Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble()));
            chartData.getDatasets().add(chartDatasetDiesel);
        }
        if(carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getEngineType().equals("электро"))) {
            chartDatasetElectric.setType("bar");
            chartDatasetElectric.setBackgroundColor("rgba(70, 157, 139, 0.8)");
            chartDatasetElectric.setLabel("электро");
            chartDatasetElectric.setOrder(2);
            chartDatasetElectric.getData().add(0);
            chartDatasetElectric.getData().add(Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble()));
            chartData.getDatasets().add(chartDatasetElectric);
        }

        if(carAdverts.stream()
                .anyMatch(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))) {
            chartDatasetHybrid.setType("bar");
            chartDatasetHybrid.setBackgroundColor("rgba(234, 137, 51, 0.8)");
            chartDatasetHybrid.setLabel("гибрид");
            chartDatasetHybrid.setOrder(2);
            chartDatasetHybrid.getData().add(0);
            chartDatasetHybrid.getData().add(Math.round((float)
                    carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                            .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                            .average().getAsDouble()));
            chartData.getDatasets().add(chartDatasetHybrid);
        }

        chartDatasetAverage.setType("line");
        chartDatasetAverage.setBackgroundColor("rgba(223, 60, 34, 0.8)");
        chartDatasetAverage.setLabel(localizationService.getMessage(AVERAGE_PRICE_MESSAGE_KEY));
        chartDatasetAverage.setOrder(1);
        int averagePrice = Math.round((float)
                carAdverts.stream()
                        .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                        .average().getAsDouble());
        int i = 1;
        for (int j = 0; j < 2; j++) {

            chartDatasetAverage.getData().add(averagePrice);
            chartData.getLabels().add(String.valueOf(i++));
        }

        chartData.getLabels().add(String.valueOf(i++));
        chartDatasetAverage.getData().add(averagePrice);
        chartData.getDatasets().add(chartDatasetAverage);


        priceChart.setType("line");
        priceChart.setData(chartData);
        return priceChart;
    }

    @Override
    public Chart getEngineCapacityPriceChart() {
        Chart priceChart = new Chart();
        ChartData chartData = new ChartData();

        long gasolineEngineCapacitiesCount = carAdverts.stream()
                .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                .map(CarAdvert::getEngineCapacity)
                .distinct()
                .count();
        long dieselEngineCapacitiesCount = carAdverts.stream()
                .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                .map(CarAdvert::getEngineCapacity)
                .distinct()
                .count();
        long hybridEngineCapacitiesCount = carAdverts.stream()
                .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                .map(CarAdvert::getEngineCapacity)
                .distinct()
                .count();
        long electricEngineCount = carAdverts.stream()
                .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                .distinct()
                .count();

        if(gasolineEngineCapacitiesCount != 0){
            int color = 152;
            for (int i = 0; i < gasolineEngineCapacitiesCount; i++) {
                List<String> engineCapacity = new ArrayList<>(carAdverts.stream()
                        .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                        .map(CarAdvert::getEngineCapacity)
                        .distinct()
                        .sorted(Comparator.comparingDouble(Double::parseDouble))
                        .collect(Collectors.toList()));
                ChartDatasets chartDataset = new ChartDatasets();

                chartDataset.setType("bar");

                chartDataset.setBackgroundColor("rgba(" +color+ ", 156, 237, 1.0)");
                color=color+40;
                chartDataset.setLabel(engineCapacity.get(i) + " " + localizationService.getMessage(GASOLINE_ENGINE_MESSAGE_KEY));
                chartDataset.setOrder(2);
                chartDataset.getData().add(0);
                int finalI = i;
                chartDataset.getData().add(Math.round((float)
                        carAdverts.stream()
                                .filter(carAdvert -> carAdvert.getEngineType().equals("бензин"))
                                .filter(carAdvert -> carAdvert.getEngineCapacity().equals(engineCapacity.get(finalI)))
                                .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                                .average().getAsDouble()));

                chartData.getDatasets().add(chartDataset);
            }
        }
        if(dieselEngineCapacitiesCount != 0) {
            int color = 25;
            for (int i = 0; i < dieselEngineCapacitiesCount; i++) {
                List<String> engineCapacity = new ArrayList<>(carAdverts.stream()
                        .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                        .map(CarAdvert::getEngineCapacity)
                        .distinct()
                        .sorted(Comparator.comparingDouble(Double::parseDouble))
                        .collect(Collectors.toList()));
                ChartDatasets chartDataset = new ChartDatasets();

                chartDataset.setType("bar");

                chartDataset.setBackgroundColor("rgba(230, "+color+", " + color + ", 1.0)");
                color = color + 40;
                chartDataset.setLabel(engineCapacity.get(i) + " " + localizationService.getMessage(DIESEL_FUEL_ENGINE_MESSAGE_KEY));
                chartDataset.setOrder(2);
                chartDataset.getData().add(0);
                int finalI = i;
                chartDataset.getData().add(Math.round((float)
                        carAdverts.stream()
                                .filter(carAdvert -> carAdvert.getEngineType().equals("дизель"))
                                .filter(carAdvert -> carAdvert.getEngineCapacity().equals(engineCapacity.get(finalI)))
                                .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                                .average().getAsDouble()));

                chartData.getDatasets().add(chartDataset);
            }
        }
        if (hybridEngineCapacitiesCount != 0) {
                int color = 137;
                for (int i = 0; i < hybridEngineCapacitiesCount; i++) {
                    List<String> engineCapacity = new ArrayList<>(carAdverts.stream()
                            .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                            .map(CarAdvert::getEngineCapacity)
                            .distinct()
                            .sorted(Comparator.comparingDouble(Double::parseDouble))
                            .collect(Collectors.toList()));
                    ChartDatasets chartDataset = new ChartDatasets();

                    chartDataset.setType("bar");

                    chartDataset.setBackgroundColor("rgba(234, "+color+", 51, 1.0)");
                    color=color+40;
                    chartDataset.setLabel(engineCapacity.get(i) + " " + localizationService.getMessage(HYBRID_ENGINE_MESSAGE_KEY));
                    chartDataset.setOrder(2);
                    chartDataset.getData().add(0);
                    int finalI = i;
                    chartDataset.getData().add(Math.round((float)
                            carAdverts.stream()
                                    .filter(carAdvert -> carAdvert.getEngineType().equals("бензин (гибрид)"))
                                    .filter(carAdvert -> carAdvert.getEngineCapacity().equals(engineCapacity.get(finalI)))
                                    .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                                    .average().getAsDouble()));

                    chartData.getDatasets().add(chartDataset);
                }
            }

        if(electricEngineCount != 0){
            for (int i = 0; i < gasolineEngineCapacitiesCount; i++) {
                List<String> engineCapacity = new ArrayList<>(carAdverts.stream()
                        .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                        .map(CarAdvert::getEngineCapacity)
                        .distinct()
                        .collect(Collectors.toList()));
                ChartDatasets chartDataset = new ChartDatasets();

                chartDataset.setType("bar");
                chartDataset.setBackgroundColor("rgba(70, 157, 139, 0.8)");
                chartDataset.setLabel(engineCapacity.get(i));
                chartDataset.setOrder(2);
                chartDataset.getData().add(0);
                int finalI = i;
                chartDataset.getData().add(Math.round((float)
                        carAdverts.stream()
                                .filter(carAdvert -> carAdvert.getEngineType().equals("электро"))
                                .filter(carAdvert -> carAdvert.getEngineCapacity().equals(engineCapacity.get(finalI)))
                                .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                                .average().getAsDouble()));

                chartData.getDatasets().add(chartDataset);
            }
        }

        ChartDatasets chartDatasetAverage = new ChartDatasets();
        chartDatasetAverage.setType("line");
        chartDatasetAverage.setBackgroundColor("rgba(223, 60, 34, 0.8)");
        chartDatasetAverage.setLabel(localizationService.getMessage(AVERAGE_PRICE_MESSAGE_KEY));
        chartDatasetAverage.setOrder(1);
        int averagePrice = Math.round((float)
                carAdverts.stream()
                        .mapToInt(carAdvert -> Integer.parseInt(carAdvert.getPrice()))
                        .average().getAsDouble());

        int j = 0;
        for (int i = 0; i < 2; i++) {
            j = i+1;
            chartDatasetAverage.getData().add(averagePrice);
            chartData.getLabels().add(String.valueOf(j));
        }

        chartData.getLabels().add(String.valueOf(j++));
        chartDatasetAverage.getData().add(averagePrice);
        chartData.getDatasets().add(chartDatasetAverage);

        priceChart.setType("line");
        priceChart.setData(chartData);
        return priceChart;

    }

    @Override
    public List<Chart> getCharts() {

        List<Chart> charts = new ArrayList<>();
        charts.add(getTransmissionPriceChart());
        charts.add(getEngineTypePriceChart());
        charts.add (getEngineCapacityPriceChart());
        return charts;
    }

    @Override
    public void getAllAdsCarProperties() {
        carAdverts.clear();
        final RestTemplate restTemplate = new RestTemplate();
        for (String link: adsList) {
            String linkCarIdRegEx = "(?<=/)(\\d{7,})(?=\\n)";
            Pattern pattern = Pattern.compile(linkCarIdRegEx);
            Matcher matcher = pattern.matcher(link);
            matcher.find();
            do {
                setRequestPath(String.format(CAR_AD_PATH, Integer.parseInt(matcher.group(1))));
                carAdverts.add(restTemplate.getForObject(requestPath, CarAdvert.class));
            }while (matcher.find());
        }
    }

    @Override
    public void getAllAdsLinks(int brandId, int modelId, int generationId) {
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

        int pageCount = 1;
        if (matcher.find()) {
            totalAdsCount = Integer.parseInt(matcher.group(1));
            pageCount = (int) Math.ceil(totalAdsCount / 25f);
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

    @Autowired
    public AvbyApiClientImpl(LocalizationService localizationService, CarLibraryService carLibraryService) {
        this.localizationService = localizationService;
        this.carLibraryService = carLibraryService;
    }

}


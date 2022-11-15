package com.github.av.bytelegrambot;




import com.github.av.bytelegrambot.chart.Chart;

import java.util.List;

public interface AvbyApiClient {


    void getAllAdsLinks(int brandId, int modelId, int generationId);

    void getAllAdsCarProperties();

    String getCarChartsMessage();

    List<Chart> getCharts();

    List<String> getChartPhotoByURL();

    Chart getTransmissionPriceChart();

    Chart getEngineTypePriceChart();

    Chart getEngineCapacityPriceChart();


    String convertChartToJSON(Chart chart);

}

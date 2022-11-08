package com.github.av.bytelegrambot;




import java.util.List;

public interface AvbyApiClient {


    String getAllAds(int brandId, int modelId, int generationId);

    void initDB();


}

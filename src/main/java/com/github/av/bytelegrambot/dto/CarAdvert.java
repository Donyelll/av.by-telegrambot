package com.github.av.bytelegrambot.dto;


import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class CarAdvert {

    @JsonProperty("id")
    private String id;

    @JsonIgnore
    private String price;

    @JsonIgnore
    private String year;

    private String engineType;

    private String engineCapacity;

    private String transmission;

    private String mileage;

    @JsonProperty("properties")
    public void unpackAllFields(List<CarAdvertProperty> properties){

        this.price = properties.stream().filter(carProperty -> carProperty.getName().equals("price_amount_usd")).findAny().get().getValue();
        this.year = properties.stream().filter(carProperty -> carProperty.getName().equals("year")).findAny().get().getValue();
        this.engineType = properties.stream().filter(carProperty -> carProperty.getName().equals("engine_type")).findAny().get().getValue();
        if(engineType.equals("электро")) {
            this.engineCapacity = "электро";
        }else{
            this.engineCapacity = properties.stream().filter(carProperty -> carProperty.getName().equals("engine_capacity")).findAny().get().getValue();
        }
        this.transmission = properties.stream().filter(carProperty -> carProperty.getName().equals("transmission_type")).findAny().get().getValue();
        this.mileage = properties.stream().filter(carProperty -> carProperty.getName().equals("mileage_km")).findAny().get().getValue();


    }

}

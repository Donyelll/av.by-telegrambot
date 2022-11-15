package com.github.av.bytelegrambot.chart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChartData {

    private List<String> labels = new ArrayList<>();

    private List<ChartDatasets> datasets = new ArrayList<>();
}

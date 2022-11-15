package com.github.av.bytelegrambot.chart;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChartDatasets {

    private String type;

    private Boolean fill = false;

    private String cubicInterpolationMode = "monotone";

    private Integer order;

    private String backgroundColor = "rgba(223, 60, 34, 0.8)";

    private Float lineTension = 0.4f;

    private String label;

    private List<Integer> data = new ArrayList<>();


}

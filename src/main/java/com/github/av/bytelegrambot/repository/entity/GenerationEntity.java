package com.github.av.bytelegrambot.repository.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "generation")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name="model_id", nullable = false)
    private ModelEntity model;

    @ManyToOne
    @JoinColumn(name="brand_id", nullable = false)
    private BrandEntity brand;
}

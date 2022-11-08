package com.github.av.bytelegrambot.repository.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "brand")
public class BrandEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<ModelEntity> models;

    @OneToMany(mappedBy = "brand")
    private List<GenerationEntity> generations;
}

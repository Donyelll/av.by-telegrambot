package com.github.av.bytelegrambot.repository.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Model> models;

    @OneToMany(mappedBy = "brand")
    private List<Generation> generations;
}

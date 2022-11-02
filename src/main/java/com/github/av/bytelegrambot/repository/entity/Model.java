package com.github.av.bytelegrambot.repository.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "model")
public class Model {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name="brand_id", nullable = false)
    private Brand brand;

    @OneToMany(mappedBy = "model")
    private List<Generation> generations;
}

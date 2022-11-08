package com.github.av.bytelegrambot.repository.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "generation")
public class Generation {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name="model_id", nullable = false)
    private Model model;

    @ManyToOne
    @JoinColumn(name="brand_id", nullable = false)
    private Brand brand;
}

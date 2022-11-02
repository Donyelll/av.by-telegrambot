package com.github.av.bytelegrambot.repository;

import com.github.av.bytelegrambot.repository.entity.Brand;
import com.github.av.bytelegrambot.repository.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model,String> {

    Optional<Model> findByNameIs(String name);

    List<Model> findAllByBrand_id(int id);

}

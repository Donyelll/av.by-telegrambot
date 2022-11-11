package com.github.av.bytelegrambot.repository;

import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<ModelEntity,String> {

    Optional<ModelEntity> findFirstByNameIgnoreCase(String name);

    List<ModelEntity> findAllByBrand_id(int id);

    List<ModelEntity> findAllByIdNotNull();

    Optional<ModelEntity> findById(int id);
}

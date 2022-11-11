package com.github.av.bytelegrambot.repository;

import com.github.av.bytelegrambot.repository.entity.GenerationEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenerationRepository extends JpaRepository<GenerationEntity,String> {

    Optional<GenerationEntity> findByNameIgnoreCase(String name);

    List<GenerationEntity> findAllByModel_id (int id);

    List<GenerationEntity> findAllByIdNotNull();

    Optional<GenerationEntity> findById(int id);
}

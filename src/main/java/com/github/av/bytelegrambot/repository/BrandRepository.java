package com.github.av.bytelegrambot.repository;

import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity,String> {

    Optional<BrandEntity> findFirstByNameIgnoreCase(String name);

    Optional<BrandEntity> findById(int id);

    List<BrandEntity> findAllByIdNotNull();

}

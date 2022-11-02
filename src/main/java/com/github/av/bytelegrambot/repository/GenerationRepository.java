package com.github.av.bytelegrambot.repository;

import com.github.av.bytelegrambot.repository.entity.Brand;
import com.github.av.bytelegrambot.repository.entity.Generation;
import com.github.av.bytelegrambot.repository.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenerationRepository extends JpaRepository<Generation,String> {

    Optional<Generation> findByNameIs(String name);

    List<Generation> findAllByModel_id (int id);

}

package com.github.av.bytelegrambot.repository;

import com.github.av.bytelegrambot.repository.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,String> {

    Optional<Brand> findByNameIs(String name);

    Optional<Brand> findById(int id);

}

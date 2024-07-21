package com.hotspot.livfit.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.store.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Category findByName(String name);
}

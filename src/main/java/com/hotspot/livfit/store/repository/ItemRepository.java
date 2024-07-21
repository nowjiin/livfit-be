package com.hotspot.livfit.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.store.entity.Category;
import com.hotspot.livfit.store.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
  List<Item> findByCategory(Category category);
}

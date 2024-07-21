package com.hotspot.livfit.store.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.store.dto.ItemDTO;
import com.hotspot.livfit.store.entity.Category;
import com.hotspot.livfit.store.entity.Item;
import com.hotspot.livfit.store.repository.CategoryRepository;
import com.hotspot.livfit.store.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final CategoryRepository categoryRepository;
  private final ItemRepository itemRepository;

  @Transactional(readOnly = true)
  public List<ItemDTO> getItemsByCategory(String categoryName) {
    List<Item> items;
    if ("ALL".equalsIgnoreCase(categoryName)) {
      items = itemRepository.findAll();
    } else {
      Category category = categoryRepository.findByName(categoryName);
      if (category == null) {
        throw new RuntimeException("Category not found");
      }
      items = itemRepository.findByCategory(category);
    }
    return items.stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  private ItemDTO convertToDTO(Item item) {
    ItemDTO dto = new ItemDTO();
    dto.setId(item.getId());
    dto.setName(item.getName());
    dto.setPointPrice(item.getPointPrice());
    dto.setCashPrice(item.getCashPrice());
    return dto;
  }
}

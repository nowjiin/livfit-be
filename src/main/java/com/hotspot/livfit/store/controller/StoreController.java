package com.hotspot.livfit.store.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.livfit.store.dto.ItemDTO;
import com.hotspot.livfit.store.service.StoreService;

@RestController
@RequiredArgsConstructor
public class StoreController {
  private final StoreService storeService;

  @GetMapping("/api/items")
  public ResponseEntity<List<ItemDTO>> getItems(
      @RequestParam(defaultValue = "ALL") String category) {
    List<ItemDTO> items = storeService.getItemsByCategory(category);
    return ResponseEntity.ok(items);
  }
}

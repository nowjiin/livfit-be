package com.hotspot.livfit.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
  private Long id;
  private String name;
  private Long pointPrice;
  private Long cashPrice;
}

package com.hotspot.livfit.exercise.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PushupGraphDTO {
  private LocalDateTime createdAt;
  private Double graph;
}

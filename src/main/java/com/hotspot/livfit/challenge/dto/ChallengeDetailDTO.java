package com.hotspot.livfit.challenge.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDetailDTO {
  private Long id;
  private String title;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private String frequency;
  private int difficulty;
  private String reward;
}

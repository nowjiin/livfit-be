package com.hotspot.livfit.challenge.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 챌린지 상세페이지 DTO
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

package com.hotspot.livfit.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeSummaryDTO {
  private Long id;
  private String title;
  private String description;
  private String status; // 진행중, 성공, 실패
}

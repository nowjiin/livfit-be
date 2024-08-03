package com.hotspot.livfit.challenge.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 응답DTO
// 엔티티에서 정보 필요한 것만 가져오도록 설정
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeSummaryDTO {
  private Long id;
  private String title;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private String frequency;
  private int status; // 진행 전, 즉 상태 : 0으로
}

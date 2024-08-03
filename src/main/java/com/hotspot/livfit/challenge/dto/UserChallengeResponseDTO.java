package com.hotspot.livfit.challenge.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 응답 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeResponseDTO {
  private Long id;
  private String loginId;
  private String challengeTitle;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private String frequency;
  private int status; // 0: 진행 전, 1: 진행중, 2: 완료
}

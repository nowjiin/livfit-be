package com.hotspot.livfit.challenge.dto;

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
  private int status; // 0: 진행중, 1: 성공, 2: 실패 **
}

package com.hotspot.livfit.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 챌린지의 상태 바꿀 때 요청 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeUpdateRequestDTO {
  private Long challengeId;
  private int status; // 0: 진행 전, 1: 진행중, 2: 성공, 3: 실패
}

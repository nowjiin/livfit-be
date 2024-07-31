package com.hotspot.livfit.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// '참여하기' 버튼 눌러서 챌린지 참가하기 위한 DTO
// 그래서 챌린지 아이디만 요청
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeRequestDTO {
  private Long challengeId;
}

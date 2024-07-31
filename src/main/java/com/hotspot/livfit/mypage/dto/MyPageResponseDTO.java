package com.hotspot.livfit.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyPageResponseDTO {
  private String loginId;
  private String nickname;
  private int totalPoints;
  private String mainBadgeId; // 메인 뱃지 ID
  private int badgeCount; // 사용자가 소유한 뱃지 개수 추가
}

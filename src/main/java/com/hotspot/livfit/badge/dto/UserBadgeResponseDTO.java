package com.hotspot.livfit.badge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBadgeResponseDTO {
  private Long id;
  private String loginId;
  private String nickname;
  private String badgeId;
  private String earnedTime;
  private boolean mainBadge; // 메인 뱃지 여부

  public UserBadgeResponseDTO(
      Long id,
      String loginId,
      String nickname,
      String badgeId,
      String earnedTime,
      boolean mainBadge) {
    this.id = id;
    this.loginId = loginId;
    this.nickname = nickname;
    this.badgeId = badgeId;
    this.earnedTime = earnedTime;
    this.mainBadge = mainBadge; // 메인 뱃지 여부 설정
  }
}

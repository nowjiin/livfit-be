package com.hotspot.livfit.badge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeRequestDTO {
  private String loginId; // 로그인 아이디
  private String badgeId; // 뱃지 ID
  private boolean conditionCheck; // 조건 확인 여부
}

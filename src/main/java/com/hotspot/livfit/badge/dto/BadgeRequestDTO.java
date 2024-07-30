package com.hotspot.livfit.badge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeRequestDTO {
  private String badgeId; // 뱃지 ID
  private boolean conditionCheck; // 조건 확인 여부
}

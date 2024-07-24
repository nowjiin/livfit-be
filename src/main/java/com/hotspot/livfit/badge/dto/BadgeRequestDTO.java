package com.hotspot.livfit.badge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeRequestDTO {
  private Long userId;
  private String badgeId;
  private boolean conditionCheck;
}

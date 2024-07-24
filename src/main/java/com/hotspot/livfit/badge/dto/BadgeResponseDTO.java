package com.hotspot.livfit.badge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeResponseDTO {
  private boolean success; // 뱃지 부여 성공 여부
  private String message; // 응답 메시지

  public BadgeResponseDTO(boolean success, String message) {
    this.success = success;
    this.message = message;
  }
}

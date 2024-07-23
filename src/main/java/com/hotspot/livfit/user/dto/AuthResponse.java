package com.hotspot.livfit.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
  private String accessToken;
  private String refreshToken;

  public AuthResponse(String accessToken) {
    this.accessToken = accessToken;
  }

  public AuthResponse(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}

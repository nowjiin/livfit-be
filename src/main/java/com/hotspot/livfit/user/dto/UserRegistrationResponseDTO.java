package com.hotspot.livfit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegistrationResponseDTO {
  private String loginId;
  private String nickname;
  private String message; // 회원가입 성공 메시지
}

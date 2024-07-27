package com.hotspot.livfit.mypage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameUpdateRequestDTO {
  private String loginId;
  private String nickname;
}

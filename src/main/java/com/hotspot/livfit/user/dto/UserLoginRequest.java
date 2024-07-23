package com.hotspot.livfit.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
  private String loginId;
  private String password;
}

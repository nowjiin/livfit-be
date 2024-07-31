package com.hotspot.livfit.turtle.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TurtleDTO {
  private String nickname;
  private int score;
  private LocalDate localDate;
  private String loginId;

  public TurtleDTO(String loginId, String nickname, int score, LocalDate localDate) {
    this.loginId = loginId;
    this.nickname = nickname;
    this.score = score;
    this.localDate = localDate;
  }
}

package com.hotspot.livfit.turtle.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 비회원용
public class TurtleDTO {
  private String nickname;
  private int score;
  private LocalDate localDate;

  public TurtleDTO(String nickname, int score, LocalDate localDate) {
    this.nickname = nickname;
    this.score = score;
    this.localDate = localDate;
  }
}

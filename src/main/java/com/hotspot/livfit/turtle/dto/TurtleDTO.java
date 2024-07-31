package com.hotspot.livfit.turtle.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurtleDTO {
  private String nickname;
  private int score;
  private LocalDate localDate;

  public TurtleDTO() {}

  public TurtleDTO(String nickname, int score, LocalDate localDate) {
    this.nickname = nickname;
    this.score = score;
    this.localDate = localDate;
  }
}

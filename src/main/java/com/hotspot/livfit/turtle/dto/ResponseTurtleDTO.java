package com.hotspot.livfit.turtle.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTurtleDTO {
  private int score;
  private LocalDate localDate;
  private String loginId;
  private String nickname;
}

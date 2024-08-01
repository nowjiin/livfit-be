package com.hotspot.livfit.turtle.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenTurtleDTO {
  private int score;
  private LocalDate localDate;
}

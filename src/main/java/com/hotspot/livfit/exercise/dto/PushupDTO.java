package com.hotspot.livfit.exercise.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PushupDTO {
  private String login_id;
  private long timer_sec;
  private int count;
  private int perfect;
  private int great;
  private int good;
  private LocalDateTime created_at;
  private int exercise_set;
}

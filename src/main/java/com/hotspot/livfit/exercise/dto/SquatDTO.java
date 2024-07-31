package com.hotspot.livfit.exercise.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SquatDTO {

  private String login_id;
  private long timer_sec;
  private int count;
  private int perfect;
  private int great;
  private int good;
  private LocalDateTime created_at;
  private Double graph;
  private int exercise_set;
}

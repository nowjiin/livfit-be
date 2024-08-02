package com.hotspot.livfit.exercise.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 기록 데이터 받아올 것들 담고 있는 DTO 클래스
public class RecordDTO {
  private long timer_sec;
  private int count;
  private int perfect;
  private int great;
  private int good;
  private LocalDateTime created_at;
  private int exercise_set;
}

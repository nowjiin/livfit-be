package com.hotspot.livfit.today_exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeeklyExerciseStatusDTO {
  private String dayOfWeek; // 요일 (ex. "MONDAY", "TUESDAY")
  private int status; // 상태 (0: 시작 전, 1: 성공, 2: 실패)
}

package com.hotspot.livfit.today_exercise.dto;

import lombok.Getter;
import lombok.Setter;

// 오늘의 운동 상태 기록 요청 DTO
@Getter
@Setter
public class TodayExerciseStatusRequestDTO {
  private int status; // 운동 성공 여부 (0: 시작 전, 1: 성공, 2: 실패)
}

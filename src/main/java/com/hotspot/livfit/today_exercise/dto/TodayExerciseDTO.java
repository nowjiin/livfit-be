package com.hotspot.livfit.today_exercise.dto;

import lombok.Getter;
import lombok.Setter;

// 오늘의 운동 정보주는 (메인페이지 상단) 응답 DTO
@Getter
@Setter
public class TodayExerciseDTO {
  private Long id; // 운동의 고유 ID (pk)
  private String exerciseName; // 운동 이름
  private int count; // 운동할 개수
  private int timerSec; // 운동할 시간(초)
  private int status; // 운동 상태 (0: 시작 전, 1: 성공, 2: 실패)

  // 기존 생성자 추가 (네 개의 매개변수)
  public TodayExerciseDTO(Long id, String exerciseName, int count, int timerSec) {
    this.id = id;
    this.exerciseName = exerciseName;
    this.count = count;
    this.timerSec = timerSec;
    this.status = 0; // 기본값으로 설정 (필요에 따라 수정 가능)
  }
}

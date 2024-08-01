package com.hotspot.livfit.today_exercise.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "today_exercise")
@Getter
@Setter
@NoArgsConstructor
// 오늘의 운동 db [ 운동 이름, 운동 횟수, 시간 , 기한 날짜]
public class TodayExercise {
  // pk
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // 운동 이름명
  @Column(name = "exercise_name")
  private String exercise_name;

  // 운동할 개수
  @Column(name = "count")
  private int count;

  // 운동할 시간
  @Column(name = "timer_sec")
  private int timer_sec;

  // 해당 운동이 유효한 날짜 (시작 날짜)
  @Column(name = "effective_date", nullable = true)
  private LocalDate effectiveDate;

  // 해당 운동이 만료되는 날짜 (종료 날짜)
  @Column(name = "expiration_date", nullable = true)
  private LocalDate expirationDate;

  // 직접 DB에서 데이터 넣어줄거라 생성시간 필요없음
}

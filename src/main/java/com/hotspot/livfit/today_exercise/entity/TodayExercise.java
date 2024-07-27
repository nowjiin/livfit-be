package com.hotspot.livfit.today_exercise.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

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

  // 오늘의 운동 생성된 시간 [ 48시간 후 파괴 예정 ]
  @CreationTimestamp private LocalDateTime created_at;
}

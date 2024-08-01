package com.hotspot.livfit.today_exercise.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

@Entity
@Table(name = "today_exercise_user")
@Getter
@Setter
@NoArgsConstructor
public class TodayExerciseUser {
  // pk
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 로그인 아이디 참조
  @ManyToOne
  @JoinColumn(name = "login_id", referencedColumnName = "login_id")
  private User user;

  // 오늘의 운동 pk값 참조
  @ManyToOne
  @JoinColumn(name = "today_exercise_id", referencedColumnName = "id")
  private TodayExercise todayExercise;

  // 상태
  @Column(name = "status", nullable = false)
  private int status;

  public static final int STATUS_NOT_STARTED = 0; // 시작 전
  public static final int STATUS_SUCCESS = 1; // 성공
  public static final int STATUS_FAILED = 2; // 실패

  @Column(name = "attempted_at")
  private LocalDate attemptedAt;
}

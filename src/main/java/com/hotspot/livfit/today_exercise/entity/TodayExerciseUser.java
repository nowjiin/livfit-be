package com.hotspot.livfit.today_exercise.entity;

import java.time.DayOfWeek;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

@Table(name = "today_exercise_user")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class TodayExerciseUser {
  // pk
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "loginid")
  private String loginId;

  // 개인이 여러개 기록 가지고 있으므로
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  // 요일
  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week")
  private DayOfWeek dayOfWeek;

  // 성공 실패 여부
  @Column(name = "success")
  private String success;
}

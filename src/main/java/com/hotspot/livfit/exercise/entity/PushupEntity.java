package com.hotspot.livfit.exercise.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.hotspot.livfit.user.entity.User;

@Entity
@Table(name = "pushup")
@Getter
@Setter
@NoArgsConstructor
public class PushupEntity {
  // pk
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // 한명의 사용자가 여러 기록 가지고 있으니
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  // 시간 초
  @Column(name = "timer_sec")
  private long timer_sec;

  // 개수
  @Column(name = "count")
  private int count;

  // perfect
  @Column(name = "perfect")
  private int perfect;

  // great
  @Column(name = "great")
  private int great;

  // good
  @Column(name = "good")
  private int good;

  // 그래프를 위해 시간 추가
  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private LocalDateTime created_at;

  // 세트 수 저장
  @Column(name = "exercise_set")
  private int exercise_set;

  // 그래프 값에 들어갈 총 시간
  @Column(name = "total_time")
  private int total_time;

  // 그래프 값에 들어갈 총 개수
  @Column(name = "total_coutns")
  private int total_counts;
}

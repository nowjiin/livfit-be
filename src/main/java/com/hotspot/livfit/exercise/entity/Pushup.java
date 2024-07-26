package com.hotspot.livfit.exercise.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

@Entity
@Table(name = "pushup")
@Getter
@Setter
@NoArgsConstructor
public class Pushup {
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
}

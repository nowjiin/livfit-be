package com.hotspot.livfit.exercise.entity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "pushup")
@Getter
@Setter
@NoArgsConstructor
public class Pushup {
  // pk 푸쉬업 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

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

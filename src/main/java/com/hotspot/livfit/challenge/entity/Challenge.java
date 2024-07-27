package com.hotspot.livfit.challenge.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "challenge")
@Getter
@Setter
@NoArgsConstructor
public class Challenge {
  // pk
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // 챌린지 제목
  @Column(name = "title")
  private String title;

  // 챌린지 내용
  @Column @Lob private String content;

  // 운동 이름명
  @Column(name = "exercise_name")
  private String exercise_name;

  // 운동할 개수
  @Column(name = "count")
  private int count;

  // 운동할 시간
  @Column(name = "timer_sec")
  private int timer_sec;
}

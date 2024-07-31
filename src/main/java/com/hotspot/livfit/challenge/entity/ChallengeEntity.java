package com.hotspot.livfit.challenge.entity;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "challenge")
@Getter
@Setter
@NoArgsConstructor
public class ChallengeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 챌린지명
  @Column(name = "challenge_title")
  private String title;

  // 챌린지 간단 설명
  @Column(name = "description")
  private String description;

  // 챌린지 시작날짜
  @Column(name = "start_date")
  private LocalDate startDate;

  // 챌린지 끝나는 날짜
  @Column(name = "end_date")
  private LocalDate endDate;

  // 주기 (ex. "한달간 주 1회")
  @Column(name = "frequency")
  private String frequency;

  // 난이도 (ex. 별 갯수 1~3)
  @Column(name = "difficulty")
  private int difficulty;

  // 챌린지 달성시 혜택
  @Column(name = "benefit")
  private String reward;

  // 해당 챌린지에 참여한 사용자들의 상태 정보
  @OneToMany(mappedBy = "challenge")
  private Set<UserChallengeStatus> userStatuses;
}

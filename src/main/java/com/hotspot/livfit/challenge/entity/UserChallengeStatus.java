package com.hotspot.livfit.challenge.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

@Entity
@Table(name = "user_challenge_status")
@Getter
@Setter
@NoArgsConstructor
public class UserChallengeStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 챌린지 엔티티와 다대일 관계 설정
  @ManyToOne
  @JoinColumn(name = "challenge_id", referencedColumnName = "id", nullable = false)
  private ChallengeEntity challenge;

  // 유저 엔티티와 다대일 관계 설정, loginId를 참조
  @ManyToOne
  @JoinColumn(name = "user_login_id", referencedColumnName = "login_Id", nullable = false)
  private User user;

  // 챌린지 시작 날짜
  @Column(name = "started_at", nullable = false)
  private LocalDate startedAt;

  // 챌린지 상태 (0: 진행중, 1: 성공, 2: 실패)
  @Column(name = "status", nullable = false)
  private int status;

  @Column(nullable = false)
  private LocalDateTime joinedAt;
}

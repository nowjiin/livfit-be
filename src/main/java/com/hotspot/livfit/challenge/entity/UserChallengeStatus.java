package com.hotspot.livfit.challenge.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

// 해당 테이블을 생성함에 따라 challenge_user 테이블 삭제 필요
@Entity
@Table(name = "user_challenge_status")
@Getter
@Setter
@NoArgsConstructor
public class UserChallengeStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 챌린지 pk 참조
  @ManyToOne
  @JoinColumn(name = "challenge_id", referencedColumnName = "id", nullable = false)
  private ChallengeEntity challenge;

  // 유저 loginId를 참조
  @ManyToOne
  @JoinColumn(name = "user_login_id", referencedColumnName = "login_Id", nullable = false)
  private User user;

  // 챌린지 시작 날짜
  @Column(name = "started_at", nullable = false)
  private LocalDate startedAt;

  // 챌린지 상태 0: 진행 전, 1: 진행중, 2: 완료
  @Column(name = "status", nullable = false)
  private int status;

  // 챌린지 언제 참여했는지
  @Column(nullable = false)
  private LocalDate joinedAt;
}

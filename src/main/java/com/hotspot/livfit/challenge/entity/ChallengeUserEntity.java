package com.hotspot.livfit.challenge.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

@Table(name = "challenge_user")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChallengeUserEntity {

  // pk
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 개인 여러개 챌린지 기록을 가지고 있으므로
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  // 챌린지 내용 제목 가져오기
  @ManyToOne
  @JoinColumn(name = "challenge_id", referencedColumnName = "id")
  private ChallengeEntity challenge;
  // 챌린지 시작한 날짜
  @Column(name = "started_at")
  private LocalDateTime startedAt;

  // 성공 실패 여부
  @Column(name = "success")
  private String success;
}
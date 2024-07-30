package com.hotspot.livfit.badge.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

@Entity
@Table(name = "user_badge")
@Getter
@Setter
@NoArgsConstructor
public class UserBadge {
  // 그냥 PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 유저 id (로그인 아이디 참조)
  @ManyToOne // 한명의 사용자가 여러개의 뱃지를 소유할 수 있으니 다대일
  @JoinColumn(name = "login_id", referencedColumnName = "login_id") // 로그인 아이디 참조로 수정
  private User user;

  // 뱃지 id (PK 참조)
  @ManyToOne // 여러 사용자?UserBadge?가 하나의 뱃지를 참조할 수 있으니 다대일
  @JoinColumn(name = "badge_id", referencedColumnName = "id")
  private Badge badge;

  // 뱃지 획득 시간
  @Column(name = "earned_time", nullable = false)
  private LocalDateTime earnedTime;

  // 메인 뱃지 여부
  @Column(name = "main_badge", nullable = false)
  private Boolean mainBadge = false; // 기본값을 false로 설정
}

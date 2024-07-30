package com.hotspot.livfit.challenge.entity;

import java.time.LocalDateTime;

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
  // pk (챌린지 ID)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 챌린지 제목
  @Column(nullable = false)
  private String title;

  // 챌린지 내용
  @Column @Lob private String content;

  // 생성된 날짜
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

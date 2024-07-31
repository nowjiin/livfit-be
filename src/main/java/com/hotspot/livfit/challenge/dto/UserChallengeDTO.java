package com.hotspot.livfit.challenge.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 응답 dto
@Getter
@Setter
@AllArgsConstructor
public class UserChallengeDTO {
  private Long id;
  private String loginId;
  private String nickname;
  private String title;
  private String content;
  private LocalDateTime startedAt;
  private String success;
}

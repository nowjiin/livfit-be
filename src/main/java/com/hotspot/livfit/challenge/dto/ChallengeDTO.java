package com.hotspot.livfit.challenge.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeDTO {
  private String loginId;
  private LocalDateTime startedAt;
  private String success;
  private String Title;
}

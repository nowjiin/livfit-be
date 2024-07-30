package com.hotspot.livfit.mypage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.hotspot.livfit.challenge.entity.ChallengeEntity;
import com.hotspot.livfit.exercise.dto.LungeDTO;
import com.hotspot.livfit.exercise.dto.PushupDTO;
import com.hotspot.livfit.exercise.dto.SquatDTO;

@Getter
@Setter
@AllArgsConstructor
public class MyPageResponseDTO {
  private String loginId;
  private String nickname;
  private int totalPoints;
  private int badgeCount; // 사용자가 소유한 뱃지 개수 추가
  private List<LungeDTO> lunges; // Entity -> DTO로 변경
  private List<PushupDTO> pushups; // Entity -> DTO로 변경
  private List<SquatDTO> squats; // Entity -> DTO로 변경
  private List<ChallengeEntity> challengeEntities;
}

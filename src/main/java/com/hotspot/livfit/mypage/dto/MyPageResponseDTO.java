package com.hotspot.livfit.mypage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.hotspot.livfit.challenge.entity.ChallengeEntity;

@Getter
@Setter
@AllArgsConstructor
public class MyPageResponseDTO {
  private String loginId;
  private String nickname;
  private int totalPoints;
  private int badgeCount; // 사용자가 소유한 뱃지 개수 추가
  private List<ChallengeEntity> challengeEntities;
}

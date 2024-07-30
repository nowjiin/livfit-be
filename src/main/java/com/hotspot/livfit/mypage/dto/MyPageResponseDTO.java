package com.hotspot.livfit.mypage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.hotspot.livfit.challenge.entity.ChallengeEntity;
import com.hotspot.livfit.exercise.entity.LungeEntity;
import com.hotspot.livfit.exercise.entity.PushupEntity;
import com.hotspot.livfit.exercise.entity.SquatEntity;

@Getter
@Setter
@AllArgsConstructor
public class MyPageResponseDTO {
  private String loginId;
  private String nickname;
  private int totalPoints;
  private List<LungeEntity> lunges;
  private List<PushupEntity> pushups;
  private List<SquatEntity> squats;
  private List<ChallengeEntity> challengeEntities;
  private int badgeCount; // 사용자가 소유한 뱃지 개수 추가
}

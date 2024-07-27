package com.hotspot.livfit.mypage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.hotspot.livfit.challenge.entity.Challenge;
import com.hotspot.livfit.exercise.entity.Lunge;
import com.hotspot.livfit.exercise.entity.Pushup;
import com.hotspot.livfit.exercise.entity.Squat;

// 우선 이정도로만 넣어놓았고 더 필요할 시 추가
@Getter
@Setter
@AllArgsConstructor
public class MyPageResponseDTO {
  private String loginId;
  private String nickname;
  private int totalPoints;
  private List<Lunge> lunges;
  private List<Pushup> pushups;
  private List<Squat> squats;
  private List<Challenge> challenges;
}

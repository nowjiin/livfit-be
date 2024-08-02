package com.hotspot.livfit.point.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PointHistoryDTO {
  private Long id;
  private String loginId; // 로그인 ID
  private String nickname; // 닉네임
  private int points; // 적립/차감된 포인트 값 추가
  private int totalPoints; // 누적 포인트
  private String type; // 포인트 타입 (적립 or 차감)
  private String title; // 포인트 타이틀
  private String description; // 포인트 설명
  private LocalDate eventDate; // 포인트 이벤트 발생 날짜
}

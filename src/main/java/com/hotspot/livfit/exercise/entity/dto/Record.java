package com.hotspot.livfit.exercise.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 기록 데이터를 담고 있는 DTO 클래스
public class Record {
  private long timer_sec;
  private int count;
  private int perfect;
  private int great;
  private int good;
  private String token;
  private String username;
}

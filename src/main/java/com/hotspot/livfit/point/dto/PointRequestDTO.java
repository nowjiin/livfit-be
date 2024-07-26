package com.hotspot.livfit.point.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointRequestDTO {
  private int points; // 포인트 값
  private String type; // 포인트 타입 (EARN or SPEND)
  private String description; // 포인트 적립/차감 이유
}

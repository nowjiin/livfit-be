package com.hotspot.livfit.point.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

@Entity
@Table(name = "point_history")
@Getter
@Setter
@NoArgsConstructor
public class PointHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // loginId 참조
  @ManyToOne
  @JoinColumn(name = "login_id", referencedColumnName = "login_id")
  private User user;

  // 이벤트 발생 시간
  @Column(name = "event_time")
  private LocalDateTime eventTime;

  // 적립/차감된 포인트 값
  @Column(name = "points")
  private int points;

  // 누적 포인트 값
  @Column(name = "total_points")
  private int totalPoints;

  // 포인트 적립/차감 타입 (EARN or SPEND)
  @Column(name = "type")
  private String type;

  // 포인트 적립/차감에 대한 설명
  @Column(name = "description")
  private String description;
}

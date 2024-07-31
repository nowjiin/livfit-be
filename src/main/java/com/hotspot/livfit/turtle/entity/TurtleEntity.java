package com.hotspot.livfit.turtle.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import com.hotspot.livfit.user.entity.User;

@Entity
@Table(name = "turtle")
@Getter
@Setter
@NoArgsConstructor
public class TurtleEntity {

  // PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // 한명의 사용자가 여러 기록 가지고 있으니
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  // nickname
  @Column(name = "nickname")
  private String nickname;

  // score
  @Column(name = "score")
  private int score;

  // 날짜
  @Column(name = "date")
  private LocalDate localDate;
}

package com.hotspot.livfit.badge.entity;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "badge")
@Getter
@Setter
@NoArgsConstructor
public class Badge {
  // 뱃지 id (PK)
  @Id private String id; // 기본 키는 String 타입

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString(); // ID를 UUID로 자동 생성
    }
  }
}

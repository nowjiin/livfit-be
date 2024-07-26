package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.exercise.entity.Pushup;

public interface PushupRepository extends JpaRepository<Pushup, Long> {
  List<Pushup> findByUserId(Long userId); // 특정 유저의 기록을 조회하는 메서드
}
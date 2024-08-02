package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.exercise.entity.PushupEntity;

public interface PushupRepository extends JpaRepository<PushupEntity, Long> {
  @Query("SELECT s FROM PushupEntity s WHERE s.user.loginId = :loginId")
  List<PushupEntity> findByLoginId(@Param("loginId") String loginId);

  // 날짜 순으로 기록 조회
  @Query("SELECT l FROM PushupEntity l WHERE l.user.loginId = :loginId ORDER BY l.created_at DESC")
  List<PushupEntity> findAllByOrderByCreatedAtDesc(@Param("loginId") String loginId);
}

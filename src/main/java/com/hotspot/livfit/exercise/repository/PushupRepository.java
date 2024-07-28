package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.exercise.dto.PushupGraphDTO;
import com.hotspot.livfit.exercise.entity.PushupEntity;

public interface PushupRepository extends JpaRepository<PushupEntity, Long> {
  @Query("SELECT s FROM PushupEntity s WHERE s.user.loginId = :loginId")
  List<PushupEntity> findByLoginId(@Param("loginId") String loginId);

  // 날짜 순으로 기록 조회
  @Query(
      "SELECT new com.hotspot.livfit.exercise.dto.PushupGraphDTO(p.created_at, p.graph) FROM PushupEntity p WHERE p.user.loginId = :loginId ORDER BY p.created_at DESC")
  List<PushupGraphDTO> findAllByOrderByCreatedAtDesc(@Param("loginId") String loginId);
}

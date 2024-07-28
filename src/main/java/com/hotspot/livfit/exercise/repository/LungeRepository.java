package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.exercise.dto.LungeGraphDTO;
import com.hotspot.livfit.exercise.entity.LungeEntity;

public interface LungeRepository extends JpaRepository<LungeEntity, Long> {
  @Query("SELECT s FROM LungeEntity s WHERE s.user.loginId = :loginId")
  List<LungeEntity> findByLoginId(@Param("loginId") String loginId);

  // 날짜 순으로 기록 조회
  @Query(
      "SELECT new com.hotspot.livfit.exercise.dto.LungeGraphDTO(p.created_at, p.graph) FROM LungeEntity p WHERE p.user.loginId = :loginId ORDER BY p.created_at DESC")
  List<LungeGraphDTO> findAllByOrderByCreatedAtDesc(@Param("loginId") String loginId);
}

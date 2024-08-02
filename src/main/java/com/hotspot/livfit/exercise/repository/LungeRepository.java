package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.exercise.entity.LungeEntity;

public interface LungeRepository extends JpaRepository<LungeEntity, Long> {
  @Query("SELECT s FROM LungeEntity s WHERE s.user.loginId = :loginId")
  List<LungeEntity> findByLoginId(@Param("loginId") String loginId);

  @Query("SELECT l FROM LungeEntity l WHERE l.user.loginId = :loginId ORDER BY l.created_at DESC")
  List<LungeEntity> findAllByOrderByCreatedAtDesc(@Param("loginId") String loginId);
}

package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.exercise.entity.PushupEntity;

public interface PushupRepository extends JpaRepository<PushupEntity, Long> {
  @Query("SELECT s FROM PushupEntity s WHERE s.user.loginId = :loginId")
  List<PushupEntity> findByLoginId(@Param("loginId") String loginId);
}

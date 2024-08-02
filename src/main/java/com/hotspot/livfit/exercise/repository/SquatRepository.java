package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hotspot.livfit.exercise.entity.SquatEntity;

@Repository
public interface SquatRepository extends JpaRepository<SquatEntity, Long> {
  // 로그인 아이디로 스쿼트 기록 조회
  @Query("SELECT s FROM SquatEntity s WHERE s.user.loginId = :loginId")
  List<SquatEntity> findByLoginId(@Param("loginId") String loginId);

  // 날짜 순으로 기록 조회
  @Query("SELECT l FROM SquatEntity l WHERE l.user.loginId = :loginId ORDER BY l.created_at DESC")
  List<SquatEntity> findAllByOrderByCreatedAtDesc(@Param("loginId") String loginId);
}

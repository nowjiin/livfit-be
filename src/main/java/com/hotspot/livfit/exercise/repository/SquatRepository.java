package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hotspot.livfit.exercise.entity.Squat;

@Repository
public interface SquatRepository extends JpaRepository<Squat, Long> {
  // 로그인 아이디로 스쿼트 기록 조회
  @Query("SELECT s FROM Squat s WHERE s.user.loginId = :loginId")
  List<Squat> findByLoginId(String loginId);
}

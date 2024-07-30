package com.hotspot.livfit.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.challenge.entity.ChallengeUserEntity;

public interface ChallengeUserRepository extends JpaRepository<ChallengeUserEntity, Long> {

  // 챌린지 기록 loginid 로 찾기
  @Query("SELECT s FROM ChallengeUserEntity  s WHERE s.user.loginId = :loginId")
  List<ChallengeUserEntity> findByLoginId(@Param("loginId") String loginId);
}

package com.hotspot.livfit.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.challenge.entity.ChallengeUserEntity;

public interface ChallengeUserRepository extends JpaRepository<ChallengeUserEntity, Long> {
  @Query("SELECT cu FROM ChallengeUserEntity cu WHERE cu.user.loginId = :loginId")
  List<ChallengeUserEntity> findByLoginId(@Param("loginId") String loginId);
}

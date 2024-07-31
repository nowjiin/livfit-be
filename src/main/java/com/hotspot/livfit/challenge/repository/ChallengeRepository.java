package com.hotspot.livfit.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotspot.livfit.challenge.entity.ChallengeEntity;

@Repository
public interface ChallengeRepository extends JpaRepository<ChallengeEntity, Long> {
  // 모든 챌린지 조회
  List<ChallengeEntity> findAll();
}

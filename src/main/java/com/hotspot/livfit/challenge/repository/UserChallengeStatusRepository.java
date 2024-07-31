package com.hotspot.livfit.challenge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotspot.livfit.challenge.entity.UserChallengeStatus;

@Repository
public interface UserChallengeStatusRepository extends JpaRepository<UserChallengeStatus, Long> {

  // 특정 사용자의 모든 챌린지 상태를 조회
  List<UserChallengeStatus> findByUser_LoginId(String loginId);

  // 특정 사용자와 특정 챌린지에 대한 상태를 조회
  Optional<UserChallengeStatus> findByUser_LoginIdAndChallenge_Id(String loginId, Long challengeId);

  // 진행중인 챌린지를 조회하기 위한 메서드 추가
  List<UserChallengeStatus> findByUser_LoginIdAndStatus(String loginId, int status);
}

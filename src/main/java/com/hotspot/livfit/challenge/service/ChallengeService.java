package com.hotspot.livfit.challenge.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.challenge.dto.*;
import com.hotspot.livfit.challenge.entity.*;
import com.hotspot.livfit.challenge.repository.*;
import com.hotspot.livfit.point.service.PointService;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final UserChallengeStatusRepository userChallengeStatusRepository;
  private final UserRepository userRepository;
  private final PointService pointService;

  // 모든 챌린지 조회 -> 로그인에 따라 상태 반영
  @Transactional(readOnly = true)
  public List<ChallengeSummaryDTO> findAllChallengesWithUserStatus(String loginId) {
    List<ChallengeEntity> challenges = challengeRepository.findAll();
    List<ChallengeSummaryDTO> challengeDTOs = new ArrayList<>();

    for (ChallengeEntity challenge : challenges) {
      ChallengeSummaryDTO dto =
          new ChallengeSummaryDTO(
              challenge.getId(),
              challenge.getTitle(),
              challenge.getDescription(),
              challenge.getStartDate(),
              challenge.getEndDate(),
              challenge.getFrequency(),
              challenge.getStatus());
      if (loginId != null) {
        Optional<UserChallengeStatus> userStatus =
            userChallengeStatusRepository.findByUser_LoginIdAndChallenge_Id(
                loginId, challenge.getId());

        if (userStatus.isPresent()) {
          dto.setStatus(userStatus.get().getStatus());
        }
      }
      challengeDTOs.add(dto);
    }

    return challengeDTOs;
  }

  // ID로 특정 챌린지 조회
  @Transactional(readOnly = true)
  public Optional<ChallengeDetailDTO> getChallenge(Long id) {
    return challengeRepository
        .findById(id)
        .map(
            challenge ->
                new ChallengeDetailDTO(
                    challenge.getId(),
                    challenge.getTitle(),
                    challenge.getDescription(),
                    challenge.getStartDate(),
                    challenge.getEndDate(),
                    challenge.getFrequency(),
                    challenge.getDifficulty(),
                    challenge.getReward(),
                    challenge.getCertificate(),
                    challenge.getStatus()));
  }

  // 특정 사용자의 챌린지 기록 조회
  @Transactional(readOnly = true)
  public List<UserChallengeResponseDTO> getUserChallenges(String loginId) {
    List<UserChallengeStatus> statusList =
        userChallengeStatusRepository.findByUser_LoginId(loginId);
    List<UserChallengeResponseDTO> responseDTOList = new ArrayList<>();

    for (UserChallengeStatus status : statusList) {
      UserChallengeResponseDTO dto =
          new UserChallengeResponseDTO(
              status.getId(),
              status.getUser().getLoginId(),
              status.getChallenge().getTitle(),
              status.getChallenge().getDescription(),
              status.getChallenge().getStartDate(),
              status.getChallenge().getEndDate(),
              status.getChallenge().getFrequency(),
              status.getStatus() // 상태 정보 0, 1, 2 중
              );
      responseDTOList.add(dto);
    }

    return responseDTOList;
  }

  // 챌린지 참여 서비스 로직
  @Transactional
  public String participateInChallenge(String loginId, Long challengeId) {
    Optional<UserChallengeStatus> existingStatus =
        // 로그인 아이디와 챌린지 아이디로 검색해서 해당 사용자가 참여중인 챌린지인지 확인
        userChallengeStatusRepository.findByUser_LoginIdAndChallenge_Id(loginId, challengeId);

    if (existingStatus.isPresent()) {
      return "Already participating in this challenge.";
    }

    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    ChallengeEntity challenge =
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new RuntimeException("Challenge not found"));

    UserChallengeStatus newStatus = new UserChallengeStatus();
    newStatus.setUser(user);
    newStatus.setChallenge(challenge);
    newStatus.setStatus(1); // 상태를 "진행중"으로 설정 (0 은 진행 전을 의미)
    newStatus.setStartedAt(LocalDate.now());
    newStatus.setJoinedAt(LocalDate.now()); // 현재 시간으로 설정

    userChallengeStatusRepository.save(newStatus);
    return "Challenge participation successful.";
  }

  // 챌린지 상태 업데이트 (진행 전 -> 진행 중 -> 완료)
  @Transactional
  public boolean updateChallengeStatus(String loginId, Long challengeId) {
    Optional<UserChallengeStatus> statusOpt =
        userChallengeStatusRepository.findByUser_LoginIdAndChallenge_Id(loginId, challengeId);

    if (statusOpt.isPresent()) {
      UserChallengeStatus status = statusOpt.get();
      int currentStatus = status.getStatus();
      if (currentStatus == 0) { // 진행 전 상태 -> 진행 중
        status.setStatus(1);
        userChallengeStatusRepository.save(status);
        return true;
      } else if (currentStatus == 1) { // 진행 중 상태 -> 완료
        status.setStatus(2);
        userChallengeStatusRepository.save(status);
        pointService.rewardPointsForChallengeCompletion(loginId, 300); // 300 포인트 지급
        return true;
      } else if (currentStatus == 2) { // 이미 완료된 경우
        return false;
      }
    }
    return false; // 챌린지 상태가 없거나 이미 완료된 경우
  }

  // 참여중인 챌린지 서비스 로직
  @Transactional(readOnly = true)
  public List<UserChallengeResponseDTO> getInProgressChallenges(String loginId) {
    List<UserChallengeStatus> inProgressStatuses =
        userChallengeStatusRepository.findByUser_LoginIdAndStatus(loginId, 1); // 상태가 1인 것들
    List<UserChallengeResponseDTO> responseDTOList = new ArrayList<>();

    for (UserChallengeStatus status : inProgressStatuses) {
      UserChallengeResponseDTO dto = new UserChallengeResponseDTO();
      dto.setId(status.getId());
      dto.setLoginId(status.getUser().getLoginId());
      dto.setChallengeTitle(status.getChallenge().getTitle());
      dto.setDescription(status.getChallenge().getDescription());
      dto.setStartDate(status.getChallenge().getStartDate());
      dto.setEndDate(status.getChallenge().getEndDate());
      dto.setFrequency(status.getChallenge().getFrequency());
      dto.setStatus(status.getStatus());

      responseDTOList.add(dto);
    }

    return responseDTOList;
  }
}

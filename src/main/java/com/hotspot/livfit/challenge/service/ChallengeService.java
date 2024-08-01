package com.hotspot.livfit.challenge.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.challenge.dto.*;
import com.hotspot.livfit.challenge.entity.*;
import com.hotspot.livfit.challenge.repository.*;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final UserChallengeStatusRepository userChallengeStatusRepository;
  private final UserRepository userRepository;

  // 모든 챌린지 조회
  @Transactional(readOnly = true)
  public List<ChallengeSummaryDTO> findAllChallenges() {
    List<ChallengeEntity> challenges = challengeRepository.findAll();
    return challenges.stream()
        .map(
            challenge ->
                new ChallengeSummaryDTO(
                    challenge.getId(), challenge.getTitle(), challenge.getDescription(), "진행중"))
        .collect(Collectors.toList());
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
                    challenge.getReward()));
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
              status.getStatus() // 상태 정보 0, 1, 2 중
              );
      responseDTOList.add(dto);
    }

    return responseDTOList;
  }

  // 챌린지 상태 업데이트 (성공/실패 여부 업데이트)
  @Transactional
  public boolean updateChallengeStatus(String loginId, UserChallengeUpdateRequestDTO dto) {
    // 사용자의 특정 챌린지 상태를 조회
    Optional<UserChallengeStatus> statusOpt =
        userChallengeStatusRepository.findByUser_LoginIdAndChallenge_Id(
            loginId, dto.getChallengeId());

    // 챌린지 상태가 존재하는 경우 업데이트
    if (statusOpt.isPresent()) {
      UserChallengeStatus status = statusOpt.get();
      status.setStatus(dto.getStatus()); // 상태 업데이트
      userChallengeStatusRepository.save(status); // 상태 저장
      return true; // 업데이트 성공
    } else {
      return false; // 상태가 존재하지 않음
    }
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
    newStatus.setStatus(0); // 상태를 "진행중"으로 설정 (0 은 진행중 의미)
    newStatus.setStartedAt(LocalDate.now());
    newStatus.setJoinedAt(LocalDate.now()); // 현재 시간으로 설정

    userChallengeStatusRepository.save(newStatus);
    return "Challenge participation successful.";
  }

  // 참여중인 챌린지 서비스 로직
  @Transactional(readOnly = true)
  public List<UserChallengeResponseDTO> getInProgressChallenges(String loginId) {
    List<UserChallengeStatus> inProgressStatuses =
        userChallengeStatusRepository.findByUser_LoginIdAndStatus(loginId, 0); // 상태가 0인 것들
    List<UserChallengeResponseDTO> responseDTOList = new ArrayList<>();

    for (UserChallengeStatus status : inProgressStatuses) {
      UserChallengeResponseDTO dto = new UserChallengeResponseDTO();
      dto.setId(status.getId());
      dto.setLoginId(status.getUser().getLoginId());
      dto.setChallengeTitle(status.getChallenge().getTitle());
      dto.setStatus(status.getStatus());

      responseDTOList.add(dto);
    }

    return responseDTOList;
  }
}

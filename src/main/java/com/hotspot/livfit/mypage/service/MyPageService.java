package com.hotspot.livfit.mypage.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.badge.entity.UserBadge;
import com.hotspot.livfit.badge.repository.UserBadgeRepository;
import com.hotspot.livfit.challenge.dto.UserChallengeDTO;
import com.hotspot.livfit.challenge.entity.ChallengeUserEntity;
import com.hotspot.livfit.challenge.repository.ChallengeUserRepository;
import com.hotspot.livfit.mypage.dto.MyPageResponseDTO;
import com.hotspot.livfit.point.entity.PointHistory;
import com.hotspot.livfit.point.repository.PointHistoryRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final UserRepository userRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final ChallengeUserRepository challengeUserRepository;
  private final UserBadgeRepository userBadgeRepository;

  @Transactional(readOnly = true)
  public MyPageResponseDTO getMyPageInfo(String loginId) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 특정 사용자 ID의 누적 포인트 히스토리 조회하고
    int totalPoints = 0;
    List<PointHistory> history = pointHistoryRepository.findByUser_LoginId(loginId);
    // 히스토리가 비어있지 않다면, 마지막 히스토리의 totalPoints 값을 가져오도록 짰습니다
    if (!history.isEmpty()) {
      totalPoints = history.get(history.size() - 1).getTotalPoints();
    }

    // 뱃지 개수 조회 추가
    int badgeCount = userBadgeRepository.countByLoginId(loginId);

    // 메인 뱃지 조회
    Optional<UserBadge> mainBadgeOpt = userBadgeRepository.findMainBadgeByLoginId(loginId);
    String mainBadgeId =
        mainBadgeOpt.map(UserBadge::getBadge).map(badge -> badge.getId()).orElse(null);

    // 사용자의 모든 챌린지 기록 조회
    List<UserChallengeDTO> challengeUserDTOs =
        challengeUserRepository.findByLoginId(loginId).stream()
            .map(this::convertToUserChallengeDTO)
            .collect(Collectors.toList());

    return new MyPageResponseDTO(
        user.getLoginId(),
        user.getNickname(),
        totalPoints,
        mainBadgeId,
        badgeCount,
        challengeUserDTOs);
  }

  private UserChallengeDTO convertToUserChallengeDTO(ChallengeUserEntity entity) {
    return new UserChallengeDTO(
        entity.getId(),
        entity.getUser().getLoginId(),
        entity.getUser().getNickname(),
        entity.getChallenge().getTitle(),
        entity.getChallenge().getContent(),
        entity.getStartedAt(),
        entity.getSuccess());
  }

  // 닉네임 업데이트
  @Transactional
  public void updateNickname(String loginId, String newNickname) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    user.setNickname(newNickname);
    userRepository.save(user);
  }
}

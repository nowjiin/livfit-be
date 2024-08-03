package com.hotspot.livfit.mypage.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.badge.entity.UserBadge;
import com.hotspot.livfit.badge.repository.UserBadgeRepository;
import com.hotspot.livfit.challenge.dto.UserChallengeResponseDTO;
import com.hotspot.livfit.challenge.entity.UserChallengeStatus;
import com.hotspot.livfit.challenge.repository.UserChallengeStatusRepository;
import com.hotspot.livfit.point.entity.PointHistory;
import com.hotspot.livfit.point.repository.PointHistoryRepository;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final UserRepository userRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final UserBadgeRepository userBadgeRepository;
  private final UserChallengeStatusRepository userChallengeStatusRepository;

  // 누적포인트 조회
  @Transactional
  public int getTotalPoints(String loginId) {
    List<PointHistory> history = pointHistoryRepository.findByUser_LoginId(loginId);
    if (!history.isEmpty()) {
      return history.get(history.size() - 1).getTotalPoints();
    }
    return 0;
  }

  // 뱃지 개수 조회
  @Transactional
  public int getBadgeCount(String loginId) {
    return userBadgeRepository.countByLoginId(loginId);
  }

  // 메인 뱃지 조회
  @Transactional
  public String getMainBadgeId(String loginId) {
    return userBadgeRepository
        .findMainBadgeByLoginId(loginId)
        .map(UserBadge::getBadge)
        .map(badge -> badge.getId())
        .orElse(null);
  }

  // 사용자의 챌린지 기록 조회 (참여 중, 성공, 실패 포함)
  @Transactional(readOnly = true)
  public List<UserChallengeResponseDTO> getUserChallenges(String loginId) {
    List<UserChallengeStatus> statusList =
        userChallengeStatusRepository.findByUser_LoginId(loginId);
    return statusList.stream()
        .map(
            status ->
                new UserChallengeResponseDTO(
                    status.getId(),
                    status.getUser().getLoginId(),
                    status.getChallenge().getTitle(),
                    status.getChallenge().getDescription(),
                    status.getChallenge().getStartDate(),
                    status.getChallenge().getEndDate(),
                    status.getChallenge().getFrequency(),
                    status.getStatus() // 상태 정보 0, 1, 2, 3중
                    ))
        .collect(Collectors.toList());
  }
}

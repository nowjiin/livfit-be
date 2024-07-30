package com.hotspot.livfit.mypage.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.badge.repository.UserBadgeRepository;
import com.hotspot.livfit.challenge.entity.ChallengeEntity;
import com.hotspot.livfit.challenge.repository.ChallengeRepository;
import com.hotspot.livfit.exercise.entity.LungeEntity;
import com.hotspot.livfit.exercise.entity.PushupEntity;
import com.hotspot.livfit.exercise.entity.SquatEntity;
import com.hotspot.livfit.exercise.repository.LungeRepository;
import com.hotspot.livfit.exercise.repository.PushupRepository;
import com.hotspot.livfit.exercise.repository.SquatRepository;
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
  private final LungeRepository lungeRepository;
  private final PushupRepository pushupRepository;
  private final SquatRepository squatRepository;
  private final ChallengeRepository challengeRepository;
  private final UserBadgeRepository userBadgeRepository; // 추가

  @Transactional(readOnly = true)
  public MyPageResponseDTO getMyPageInfo(String loginId) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    int totalPoints = 0;
    List<PointHistory> history = pointHistoryRepository.findByUser_LoginId(loginId);
    if (!history.isEmpty()) {
      totalPoints = history.get(history.size() - 1).getTotalPoints();
    }

    List<LungeEntity> lunges = lungeRepository.findByLoginId(loginId);
    List<PushupEntity> pushups = pushupRepository.findByLoginId(loginId);
    List<SquatEntity> squats = squatRepository.findByLoginId(loginId);
    List<ChallengeEntity> challengeEntities = challengeRepository.findAllChallenges();

    // 뱃지 개수 조회 추가 (피그마 보니까 마이페이지에 뱃지 갯수 있어서 추가했습니다)
    int badgeCount = userBadgeRepository.countByLoginId(loginId);

    return new MyPageResponseDTO(
        user.getLoginId(),
        user.getNickname(),
        totalPoints,
        lunges,
        pushups,
        squats,
        challengeEntities,
        badgeCount);
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

package com.hotspot.livfit.mypage.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.challenge.entity.Challenge;
import com.hotspot.livfit.challenge.repository.ChallengeRepository;
import com.hotspot.livfit.exercise.entity.Lunge;
import com.hotspot.livfit.exercise.entity.Pushup;
import com.hotspot.livfit.exercise.entity.Squat;
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

  @Transactional(readOnly = true)
  public MyPageResponseDTO getMyPageInfo(String loginId) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 그냥 누적 포인트 조회하는 로직을 어떻게 짜야할지 모르겠어서
    // 초기 누적 포인트를 0으로 설정하고
    int totalPoints = 0;
    // 특정 사용자 ID의 누적 포인트 히스토리 조회하고
    List<PointHistory> history = pointHistoryRepository.findByUser_LoginId(loginId);
    // 히스토리가 비어있지 않다면, 마지막 히스토리의 totalPoints 값을 가져오도록 짰습니다
    if (!history.isEmpty()) {
      totalPoints = history.get(history.size() - 1).getTotalPoints();
    }

    List<Lunge> lunges = lungeRepository.findByLoginId(loginId);
    List<Pushup> pushups = pushupRepository.findByLoginId(loginId);
    List<Squat> squats = squatRepository.findByLoginId(loginId);
    List<Challenge> challenges = challengeRepository.findAllChallenges();

    return new MyPageResponseDTO(
        user.getLoginId(), user.getNickname(), totalPoints, lunges, pushups, squats, challenges);
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

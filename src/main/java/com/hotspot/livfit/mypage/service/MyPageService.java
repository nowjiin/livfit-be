package com.hotspot.livfit.mypage.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.badge.repository.UserBadgeRepository;
import com.hotspot.livfit.challenge.entity.ChallengeEntity;
import com.hotspot.livfit.challenge.repository.ChallengeRepository;
import com.hotspot.livfit.exercise.dto.LungeDTO;
import com.hotspot.livfit.exercise.dto.PushupDTO;
import com.hotspot.livfit.exercise.dto.SquatDTO;
import com.hotspot.livfit.exercise.entity.LungeEntity;
import com.hotspot.livfit.exercise.entity.PushupEntity;
import com.hotspot.livfit.exercise.entity.SquatEntity;
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
  private final ChallengeRepository challengeRepository;
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

    // 모든 챌린지 조회
    List<ChallengeEntity> challengeEntities = challengeRepository.findAllChallenges();

    // 뱃지 개수 조회 추가
    int badgeCount = userBadgeRepository.countByLoginId(loginId);

    return new MyPageResponseDTO(
            user.getLoginId(), user.getNickname(), totalPoints, badgeCount, challengeEntities);
  }

  // LungeEntity를 LungeDTO로 변환
  private LungeDTO convertToLungeDTO(LungeEntity lungeEntity) {
    LungeDTO dto = new LungeDTO();
    dto.setLogin_id(lungeEntity.getUser().getLoginId());
    dto.setTimer_sec(lungeEntity.getTimer_sec());
    dto.setCount(lungeEntity.getCount());
    dto.setPerfect(lungeEntity.getPerfect());
    dto.setGreat(lungeEntity.getGreat());
    dto.setGood(lungeEntity.getGood());
    dto.setCreated_at(lungeEntity.getCreated_at());
    dto.setGraph(lungeEntity.getGraph());
    return dto;
  }

  // PushupEntity를 PushupDTO로 변환
  private PushupDTO convertToPushupDTO(PushupEntity pushupEntity) {
    PushupDTO dto = new PushupDTO();
    dto.setLogin_id(pushupEntity.getUser().getLoginId());
    dto.setTimer_sec(pushupEntity.getTimer_sec());
    dto.setCount(pushupEntity.getCount());
    dto.setPerfect(pushupEntity.getPerfect());
    dto.setGreat(pushupEntity.getGreat());
    dto.setGood(pushupEntity.getGood());
    dto.setCreated_at(pushupEntity.getCreated_at());
    dto.setGraph(pushupEntity.getGraph());
    return dto;
  }

  // SquatEntity를 SquatDTO로 변환
  private SquatDTO convertToSquatDTO(SquatEntity squatEntity) {
    SquatDTO dto = new SquatDTO();
    dto.setLogin_id(squatEntity.getUser().getLoginId());
    dto.setTimer_sec(squatEntity.getTimer_sec());
    dto.setCount(squatEntity.getCount());
    dto.setPerfect(squatEntity.getPerfect());
    dto.setGreat(squatEntity.getGreat());
    dto.setGood(squatEntity.getGood());
    dto.setCreated_at(squatEntity.getCreated_at());
    dto.setGraph(squatEntity.getGraph());
    return dto;
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

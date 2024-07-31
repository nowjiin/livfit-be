package com.hotspot.livfit.mypage.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.badge.entity.UserBadge;
import com.hotspot.livfit.badge.repository.UserBadgeRepository;
import com.hotspot.livfit.point.entity.PointHistory;
import com.hotspot.livfit.point.repository.PointHistoryRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final UserRepository userRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final UserBadgeRepository userBadgeRepository;

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

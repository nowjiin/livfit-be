package com.hotspot.livfit.badge.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.badge.entity.Badge;
import com.hotspot.livfit.badge.entity.UserBadge;
import com.hotspot.livfit.badge.repository.BadgeRepository;
import com.hotspot.livfit.badge.repository.UserBadgeRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor // 생성자 자동 생성, 필요한 필드 주입? 역할
public class UserBadgeService {

  private final UserBadgeRepository userBadgeRepository;
  private final UserRepository userRepository;
  private final BadgeRepository badgeRepository;

  // id로 유저 엔티티 조회
  @Transactional
  public boolean checkandAwardBadge(
      Long userId, String badgeId, boolean conditionCheck) { // checkNaward -> checkandAward로 변경
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    Badge badge =
        badgeRepository
            .findById(badgeId)
            .orElseThrow(() -> new RuntimeException("Badge not found"));

    if (conditionCheck) {
      // 조건 만족 시 뱃지 부여
      awardBadgeToUser(user, badge);
      return true; // 뱃지 부여
    } else {
      return false; // 조건 만족 X
    }
  }

  // 유저뱃지 엔티티 생성&저장
  private void awardBadgeToUser(User user, Badge badge) {
    UserBadge userBadge = new UserBadge();
    userBadge.setUser(user);
    userBadge.setBadge(badge);
    userBadge.setEarnedTime(LocalDateTime.now());
    userBadgeRepository.save(userBadge);
  }

  // 특정 사용자의 뱃지를 조회
  public List<UserBadge> getUserBadges(Long userId) {
    return userBadgeRepository.findByUserId(userId);
  }
}

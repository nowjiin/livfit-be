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

  // login id로 유저 엔티티 조회
  @Transactional
  public boolean checkandAwardBadge(
      String loginId, String badgeId, boolean conditionCheck) { // checkNaward -> checkandAward로 변경
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

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
  // 메인 뱃지 설정 메서드
  @Transactional
  public void setMainBadge(String loginId, String badgeId) {
    // 로그인 ID로 사용자 조회
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 이전 메인 뱃지를 모두 해제
    List<UserBadge> userBadges = userBadgeRepository.findByLoginId(loginId);
    for (UserBadge userBadge : userBadges) {
      userBadge.setMainBadge(false);
    }
    userBadgeRepository.saveAll(userBadges);

    // 새로운 메인 뱃지 설정
    UserBadge mainBadge =
        userBadgeRepository
            .findByUser_LoginIdAndBadge_Id(loginId, badgeId)
            .orElseThrow(() -> new RuntimeException("UserBadge not found"));

    mainBadge.setMainBadge(true);
    userBadgeRepository.save(mainBadge);
  }

  // 특정 사용자의 뱃지를 조회
  public List<UserBadge> getUserBadges(String loginId) {
    return userBadgeRepository.findByLoginId(loginId);
  }
}

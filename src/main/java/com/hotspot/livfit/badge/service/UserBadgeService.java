package com.hotspot.livfit.badge.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.badge.entity.Badge;
import com.hotspot.livfit.badge.entity.UserBadge;
import com.hotspot.livfit.badge.repository.BadgeRepository;
import com.hotspot.livfit.badge.repository.UserBadgeRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserBadgeService {

  private final UserBadgeRepository userBadgeRepository;
  private final UserRepository userRepository;
  private final BadgeRepository badgeRepository;

  // 특정 뱃지를 부여 (중복 방지)
  @Transactional
  public boolean checkandAwardBadge(String loginId, String badgeId, boolean conditionCheck) {
    // loginId를 사용하여 사용자 조회
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(
                () -> {
                  log.error("User not found with login ID: {}", loginId);
                  return new RuntimeException("User not found");
                });

    // badgeId를 사용하여 Badge 조회
    Badge badge =
        badgeRepository
            .findById(badgeId)
            .orElseThrow(
                () -> {
                  log.error("Badge not found with badge ID: {}", badgeId);
                  return new RuntimeException("Badge not found");
                });

    if (conditionCheck) {
      // 사용자가 해당 뱃지를 이미 가지고 있는지 확인
      boolean alreadyHasBadge = userBadgeRepository.existsByUserAndBadge(user, badge);
      if (!alreadyHasBadge) {
        // 중복되지 않으면 뱃지를 부여
        awardBadgeToUser(user, badge);
        log.info("뱃지 '{}' 가 '{}' 사용자에게 부여됨", badgeId, loginId);
        return true;
      } else {
        log.warn("'{}' 사용자가 이미 '{}' 뱃지를 소유하고있음 | 뱃지 부여 실패", loginId, badgeId);
      }
    } else {
      log.warn(
          "Condition check failed for awarding badge '{}' to user with login ID '{}'",
          badgeId,
          loginId);
    }
    return false; // 조건을 만족하지 않거나 이미 뱃지를 소유하고 있는 경우
  }

  // 유저뱃지 엔티티 생성&저장
  private void awardBadgeToUser(User user, Badge badge) {
    log.info(
        "Creating UserBadge entity for user with ID '{}' and badge '{}'",
        user.getId(),
        badge.getId());
    UserBadge userBadge = new UserBadge();
    userBadge.setUser(user);
    userBadge.setBadge(badge);
    userBadge.setEarnedTime(LocalDateTime.now());
    userBadgeRepository.save(userBadge);

    log.info(
        "UserBadge entity saved for user with ID '{}' and badge '{}'", user.getId(), badge.getId());
  }

  // 메인 뱃지 설정 메서드
  @Transactional
  public void setMainBadge(String loginId, String badgeId) {
    // 로그인 ID로 사용자 조회
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(
                () -> {
                  log.error("User not found with login ID: {}", loginId);
                  return new RuntimeException("User not found");
                });

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
            .orElseThrow(
                () -> {
                  log.error(
                      "UserBadge not found for user with login ID '{}' and badge ID '{}'",
                      loginId,
                      badgeId);
                  return new RuntimeException("UserBadge not found");
                });

    mainBadge.setMainBadge(true);
    userBadgeRepository.save(mainBadge);
    log.info("'{}' 가 '{}'의 메인 뱃지로 설정됨", badgeId, loginId);
  }

  // 특정 사용자의 뱃지를 조회
  public List<UserBadge> getUserBadges(String loginId) {
    List<UserBadge> userBadges = userBadgeRepository.findByLoginId(loginId);
    if (userBadges.isEmpty()) {
      log.warn("No badges found for user with login ID '{}'", loginId);
    } else {
      log.info("{} 사용자의 뱃지 {}개 조회 성공", loginId, userBadges.size());
    }
    return userBadges;
  }
}

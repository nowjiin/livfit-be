package com.hotspot.livfit.badge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hotspot.livfit.badge.entity.Badge;
import com.hotspot.livfit.badge.entity.UserBadge;
import com.hotspot.livfit.user.entity.User;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
  // 로그인 아이디로 유저뱃지 조회
  @Query("SELECT ub FROM UserBadge ub WHERE ub.user.loginId = :loginId")
  List<UserBadge> findByLoginId(String loginId);

  // 로그인 아이디와 뱃지 아이디로 유저뱃지 조회 (고유한 결과 반환)
  @Query("SELECT ub FROM UserBadge ub WHERE ub.user.loginId = :loginId AND ub.badge.id = :badgeId")
  Optional<UserBadge> findByUser_LoginIdAndBadge_Id(String loginId, String badgeId);

  // 사용자와 뱃지로 UserBadge 엔티티에 존재 여부 확인
  boolean existsByUserAndBadge(User user, Badge badge);

  // 사용자가 소유한 뱃지 개수 조회 (마이페이지에서 뱃지 개수를 나타내기 위한 쿼리)
  @Query("SELECT COUNT(ub) FROM UserBadge ub WHERE ub.user.loginId = :loginId")
  int countByLoginId(String loginId);

  // 메인 뱃지 조회
  @Query("SELECT ub FROM UserBadge ub WHERE ub.user.loginId = :loginId AND ub.mainBadge = true")
  Optional<UserBadge> findMainBadgeByLoginId(String loginId);
}

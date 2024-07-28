package com.hotspot.livfit.badge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hotspot.livfit.badge.entity.UserBadge;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
  // 로그인 아이디로 유저뱃지 조회
  @Query("SELECT ub FROM UserBadge ub WHERE ub.user.loginId = :loginId")
  List<UserBadge> findByLoginId(String loginId);

  // 로그인 아이디와 뱃지 아이디로 유저뱃지 조회
  @Query("SELECT ub FROM UserBadge ub WHERE ub.user.loginId = :loginId AND ub.badge.id = :badgeId")
  Optional<UserBadge> findByUser_LoginIdAndBadge_Id(String loginId, String badgeId);
}

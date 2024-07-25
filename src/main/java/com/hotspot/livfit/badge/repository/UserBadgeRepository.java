package com.hotspot.livfit.badge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.badge.entity.UserBadge;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
  List<UserBadge> findByUserId(Long userId);
}

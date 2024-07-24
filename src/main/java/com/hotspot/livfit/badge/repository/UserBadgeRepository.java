package com.hotspot.livfit.badge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.badge.entity.UserBadge;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {}

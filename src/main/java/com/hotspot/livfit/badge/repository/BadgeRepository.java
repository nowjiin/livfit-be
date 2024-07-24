package com.hotspot.livfit.badge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.badge.entity.Badge;

public interface BadgeRepository extends JpaRepository<Badge, String> {}

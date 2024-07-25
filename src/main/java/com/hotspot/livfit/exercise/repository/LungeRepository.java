package com.hotspot.livfit.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.exercise.entity.Lunge;

public interface LungeRepository extends JpaRepository<Lunge, Long> {}

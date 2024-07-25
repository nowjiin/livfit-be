package com.hotspot.livfit.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.exercise.entity.Squat;

public interface SquatRepository extends JpaRepository<Squat, Long> {}

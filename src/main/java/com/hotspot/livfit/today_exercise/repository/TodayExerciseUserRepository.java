package com.hotspot.livfit.today_exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;

public interface TodayExerciseUserRepository extends JpaRepository<TodayExerciseUser, Long> {}

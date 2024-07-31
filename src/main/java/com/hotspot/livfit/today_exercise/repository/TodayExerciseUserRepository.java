package com.hotspot.livfit.today_exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;

public interface TodayExerciseUserRepository extends JpaRepository<TodayExerciseUser, Long> {
  List<TodayExerciseUser> findAllById(Long loginId);

  List<TodayExerciseUser> findByLoginId(String loginId);
}

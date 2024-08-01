package com.hotspot.livfit.today_exercise.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;

public interface TodayExerciseUserRepository extends JpaRepository<TodayExerciseUser, Long> {
  // 특정 사용자의 모든 운동 기록 가져오기
  List<TodayExerciseUser> findByUser_LoginId(String loginId);

  // 특정 날짜의 운동 상태 가져오기
  @Query(
      "SELECT u FROM TodayExerciseUser u WHERE u.user.loginId = :loginId AND u.attemptedAt = :date")
  Optional<TodayExerciseUser> findByLoginIdAndDate(
      @Param("loginId") String loginId, @Param("date") LocalDate date);

  // 특정 사용자의 특정 운동 기록을 가져오기
  @Query(
      "SELECT u FROM TodayExerciseUser u WHERE u.user.loginId = :loginId AND u.todayExercise.id = :exerciseId")
  Optional<TodayExerciseUser> findByUserAndExercise(
      @Param("loginId") String loginId, @Param("exerciseId") Long exerciseId);
}

package com.hotspot.livfit.today_exercise.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotspot.livfit.today_exercise.entity.TodayExercise;

public interface TodayExerciseRepository extends JpaRepository<TodayExercise, Long> {
  @Query("SELECT a from TodayExercise a WHERE a.id = :id")
  // db가 없을 경우를 대비해 optional로 작성함 (optional.empty()가 반환 됨)
  Optional<TodayExercise> findById(Long id);

  // 날짜에 따른 오늘의 운동 가져오기
  @Query("SELECT e FROM TodayExercise e WHERE e.effectiveDate = :date")
  Optional<TodayExercise> findExerciseByDate(LocalDate date);
}

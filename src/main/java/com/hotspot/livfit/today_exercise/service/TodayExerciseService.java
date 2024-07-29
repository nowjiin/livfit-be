package com.hotspot.livfit.today_exercise.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.today_exercise.entity.TodayExercise;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseRepository;

@Service
@RequiredArgsConstructor
public class TodayExerciseService {
  private final TodayExerciseRepository todayExerciseRepository;

  public Optional<TodayExercise> getTodayExercises(Long id) {
    return todayExerciseRepository.findById(id);
  }
}

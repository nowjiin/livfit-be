package com.hotspot.livfit.today_exercise.service;

import java.time.DayOfWeek;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.today_exercise.entity.TodayExercise;
import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseRepository;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseUserRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class TodayExerciseService {
  private final TodayExerciseRepository todayExerciseRepository;
  private final UserRepository userRepository;
  private final TodayExerciseUserRepository todayExerciseUserRepository;

  public Optional<TodayExercise> getTodayExercises(Long id) {
    return todayExerciseRepository.findById(id);
  }

  public TodayExerciseUser saveChallenge(String jwtLoginId, DayOfWeek dayOfWeek, String success) {
    User user =
        userRepository
            .findByLoginId(jwtLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    TodayExerciseUser todayExerciseUser = new TodayExerciseUser();
    todayExerciseUser.setUser(user);
    todayExerciseUser.setDayOfWeek(dayOfWeek);
    todayExerciseUser.setSuccess(success);

    return todayExerciseUserRepository.save(todayExerciseUser);
  }
}

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

  // 오늘의 운동 db에서 id별로 가져오기
  public Optional<TodayExercise> getTodayExercises(Long id) {
    return todayExerciseRepository.findById(id);
  }

  // 오늘의 운동 기록 저장하기
  public TodayExerciseUser saveChallenge(String jwtLoginId, DayOfWeek dayOfWeek, String success) {
    User user =
        userRepository
            .findByLoginId(jwtLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    TodayExerciseUser todayExerciseUser = new TodayExerciseUser();
    todayExerciseUser.setLoginId(user.getLoginId());
    todayExerciseUser.setDayOfWeek(dayOfWeek);
    todayExerciseUser.setSuccess(success);

    return todayExerciseUserRepository.save(todayExerciseUser);
  }
}

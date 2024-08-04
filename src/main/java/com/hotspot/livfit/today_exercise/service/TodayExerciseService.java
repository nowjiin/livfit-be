package com.hotspot.livfit.today_exercise.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;
import com.hotspot.livfit.today_exercise.dto.WeeklyExerciseStatusDTO;
import com.hotspot.livfit.today_exercise.entity.TodayExercise;
import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseRepository;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseUserRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodayExerciseService {

  private final TodayExerciseRepository todayExerciseRepository;
  private final TodayExerciseUserRepository todayExerciseUserRepository;
  private final UserRepository userRepository;

  // 오늘의 운동 조회 (아이디로)
  @Transactional(readOnly = true)
  public TodayExerciseDTO getTodayExerciseById(Long exerciseId) {
    TodayExercise exercise =
        todayExerciseRepository
            .findById(exerciseId)
            .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + exerciseId));
    return new TodayExerciseDTO(
        exercise.getId(),
        exercise.getExercise_name(),
        exercise.getExercise(),
        exercise.getCount(),
        exercise.getTimer_sec());
  }

  // 날짜별 운동 가져오기
  @Transactional(readOnly = true)
  public TodayExerciseDTO getTodayExercise(LocalDate date) {
    TodayExercise exercise =
        todayExerciseRepository
            .findExerciseByDate(date)
            .orElseThrow(() -> new RuntimeException("No exercise found for date: " + date));
    return new TodayExerciseDTO(
        exercise.getId(),
        exercise.getExercise_name(),
        exercise.getExercise(),
        exercise.getCount(),
        exercise.getTimer_sec());
  }

  // 오늘의 운동 상태 기록
  @Transactional
  public void recordExerciseStatus(String loginId, int status) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + loginId));

    // 오늘의 운동 가져오기 (현재 날짜 기준으로)
    LocalDate today = LocalDate.now();
    TodayExercise todayExercise =
        todayExerciseRepository
            .findExerciseByDate(today)
            .orElseThrow(() -> new RuntimeException("No exercise found for today: " + today));

    // User와 TodayExercise의 ID를 기반으로 기존 기록 조회
    Optional<TodayExerciseUser> existingRecord =
        todayExerciseUserRepository.findByUserAndExercise(loginId, todayExercise.getId());

    if (existingRecord.isPresent()) {
      // 이미 존재하는 기록이 있다면 상태만 업데이트
      TodayExerciseUser exerciseUser = existingRecord.get();
      exerciseUser.setStatus(status);
      todayExerciseUserRepository.save(exerciseUser);
    } else {
      // 새로운 기록을 생성
      TodayExerciseUser exerciseUser = new TodayExerciseUser();
      exerciseUser.setUser(user);
      exerciseUser.setTodayExercise(todayExercise);
      exerciseUser.setStatus(status);
      exerciseUser.setAttemptedAt(today);
      todayExerciseUserRepository.save(exerciseUser);
    }
  }

  // 특정 날짜의 운동 상태 확인
  // 미래 날짜들에 대해서는 기록이 없다면 0을 반환하고
  // 과거 날짜들에 대해서 기록이 없다면 2(실패)를 반환하도록 설정
  @Transactional(readOnly = true)
  public int getUserExerciseStatus(String loginId, LocalDate date) {
    LocalDate today = LocalDate.now();

    // 미래 날짜에 대한 처리: 아직 시작하지 않은 상태
    if (date.isAfter(today)) {
      log.info("Date {} is after today. Status: 0 (not started)", date);
      return 0; // 시작 전
    }

    Optional<TodayExerciseUser> exerciseUserOpt =
        todayExerciseUserRepository.findByLoginIdAndDate(loginId, date);

    if (exerciseUserOpt.isPresent()) {
      return exerciseUserOpt.get().getStatus();
    } else {
      // 과거 기록이 없는 경우 실패로 처리
      return 2; // 실패
    }
  }

  // 일주일 동안의 운동 상태 조회
  @Transactional(readOnly = true)
  public List<WeeklyExerciseStatusDTO> getWeeklyExerciseStatus(String loginId) {
    List<WeeklyExerciseStatusDTO> weeklyStatus = new ArrayList<>();

    // 이번 주 월요일 계산
    LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);

    // 월요일부터 일요일까지 순회
    for (int i = 0; i < 7; i++) {
      LocalDate date = monday.plusDays(i);
      int status = getUserExerciseStatus(loginId, date);
      String dayOfWeek = date.getDayOfWeek().toString();
      weeklyStatus.add(new WeeklyExerciseStatusDTO(dayOfWeek, status));
    }
    return weeklyStatus;
  }
}

package com.hotspot.livfit.mainpage.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.challenge.dto.UserChallengeResponseDTO;
import com.hotspot.livfit.challenge.repository.UserChallengeStatusRepository;
import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;
import com.hotspot.livfit.today_exercise.entity.TodayExercise;
import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseRepository;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseUserRepository;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MainPageService {

  private final TodayExerciseUserRepository todayExerciseUserRepository;
  private final UserRepository userRepository;
  private final TodayExerciseRepository todayExerciseRepository;
  private final UserChallengeStatusRepository userChallengeStatusRepository;

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
        exercise.getCount(),
        exercise.getTimer_sec());
  }

  // 사용자의 해당 날짜의 운동 상태 확인
  @Transactional(readOnly = true)
  public int getUserExerciseStatus(String loginId, LocalDate date) {
    // 주어진 날짜에 해당하는 사용자의 운동 상태 데이터를 찾음
    TodayExerciseUser exerciseUser =
        todayExerciseUserRepository
            .findByLoginIdAndDate(loginId, date)
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "No exercise status found for user: " + loginId + " on date: " + date));

    // 운동 성공 여부를 반환 (0: 시작 전, 1: 성공, 2: 실패)
    return exerciseUser.getStatus();
  }

  // 챌린지 상태 업데이트 (진행중인 챌린지)
  @Transactional(readOnly = true)
  public List<UserChallengeResponseDTO> getOngoingChallenges(String loginId) {
    return userChallengeStatusRepository.findByUser_LoginIdAndStatus(loginId, 0).stream()
        .map(
            status ->
                new UserChallengeResponseDTO(
                    status.getId(),
                    status.getUser().getLoginId(),
                    status.getChallenge().getTitle(),
                    status.getStatus()))
        .collect(Collectors.toList());
  }
}

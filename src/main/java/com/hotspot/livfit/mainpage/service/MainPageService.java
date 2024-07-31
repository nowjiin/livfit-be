package com.hotspot.livfit.mainpage.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.mainpage.dto.MainPageDTO;
import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;
import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseRepository;
import com.hotspot.livfit.today_exercise.repository.TodayExerciseUserRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MainPageService {
  private final TodayExerciseUserRepository todayExerciseUserRepository;
  private final UserRepository userRepository;
  private final TodayExerciseRepository todayExerciseRepository;

  @Transactional(readOnly = true)
  public MainPageDTO getMainPageInfo(String loginId) {
    // 사용자 정보를 가져옵니다
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + loginId));

    // 로그인 ID로 오늘의 운동 정보를 가져옴
    List<TodayExerciseUser> exerciseUsers = todayExerciseUserRepository.findByLoginId(loginId);

    // TodayExerciseUser 엔티티를 TodayExerciseDTO로 변환
    List<TodayExerciseDTO> exercises =
        exerciseUsers.stream().map(this::convertToTodayExerciseDTO).collect(Collectors.toList());

    return new MainPageDTO(exercises);
  }

  private TodayExerciseDTO convertToTodayExerciseDTO(TodayExerciseUser user) {
    TodayExerciseDTO dto = new TodayExerciseDTO();
    dto.setDayOfWeek(user.getDayOfWeek());
    dto.setSuccess(user.getSuccess());
    return dto;
  }
}

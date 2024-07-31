package com.hotspot.livfit.mainpage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.challenge.dto.UserChallengeResponseDTO;
import com.hotspot.livfit.challenge.entity.UserChallengeStatus;
import com.hotspot.livfit.challenge.repository.UserChallengeStatusRepository;
import com.hotspot.livfit.mainpage.dto.MainPageDTO;
import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;
import com.hotspot.livfit.today_exercise.entity.TodayExercise;
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
  private final UserChallengeStatusRepository userChallengeStatusRepository;

  @Transactional(readOnly = true) // 여기는 수정 안 했습니다
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

  // 랜덤으로 오늘의 운동을 가져오는 메서드
  public TodayExercise getRandomTodayExercise() {
    List<TodayExercise> exercises = todayExerciseRepository.findAll();
    if (exercises.isEmpty()) {
      throw new RuntimeException("No exercises available");
    }
    long seed = System.currentTimeMillis(); // 현재 시간
    Random random = new Random(seed); // seed 추가
    int randomIndex = random.nextInt(exercises.size());
    return exercises.get(randomIndex);
  }

  // 진행 중인 챌린지 가져오기
  @Transactional(readOnly = true)
  public List<UserChallengeResponseDTO> getOngoingChallenges(String loginId) {
    // 로그인 ID로 진행 중인 챌린지 상태를 가져옴
    List<UserChallengeStatus> statusList =
        userChallengeStatusRepository.findByUser_LoginId(loginId);
    List<UserChallengeResponseDTO> ongoingChallenges = new ArrayList<>();

    for (UserChallengeStatus status : statusList) {
      if (status.getStatus() == 0) { // 상태가 진행중인 경우
        ongoingChallenges.add(
            new UserChallengeResponseDTO(
                status.getId(),
                status.getUser().getLoginId(),
                status.getChallenge().getTitle(),
                status.getStatus()));
      }
    }

    return ongoingChallenges;
  }
}

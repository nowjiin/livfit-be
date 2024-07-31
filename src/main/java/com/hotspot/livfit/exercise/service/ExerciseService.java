package com.hotspot.livfit.exercise.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.exercise.dto.*;
import com.hotspot.livfit.exercise.entity.LungeEntity;
import com.hotspot.livfit.exercise.entity.PushupEntity;
import com.hotspot.livfit.exercise.entity.SquatEntity;
import com.hotspot.livfit.exercise.repository.LungeRepository;
import com.hotspot.livfit.exercise.repository.PushupRepository;
import com.hotspot.livfit.exercise.repository.SquatRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ExerciseService {
  private final LungeRepository lungeRepository;
  private final PushupRepository pushupRepository;
  private final SquatRepository squatRepository;
  private final UserRepository userRepository;

  // 그래프 계산 함수
  public static double calculatePerformance(
      int count, Long timerSec, int perfect, int great, int good) {
    // 효율성 계산
    double efficiency = timerSec > 0 ? (double) count / timerSec : 0.0; // 0으로 나누는 경우를 방지

    // 품질 점수 계산 (가중치 적용)
    double quality = (perfect * 3) + (great * 2) + good;

    // 종합 성과 지표 계산
    return efficiency + quality;
  }

  // 런지 기록 저장 로직
  public LungeEntity saveRecordLunge(
      String jwtLoginId,
      Long timerSec,
      int count,
      int perfect,
      int good,
      int great,
      LocalDateTime created_at,
      int set) {

    User user =
        userRepository
            .findByLoginId(jwtLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    LungeEntity lungeEntity = new LungeEntity();
    lungeEntity.setExercise_set(set);
    lungeEntity.setUser(user);
    lungeEntity.setTimer_sec(timerSec);
    lungeEntity.setCount(count);
    lungeEntity.setPerfect(perfect);
    lungeEntity.setGood(good);
    lungeEntity.setGreat(great);
    lungeEntity.setCreated_at(created_at);

    // 성과 지표 계산
    double graph = calculatePerformance(count, timerSec, perfect, great, good);
    lungeEntity.setGraph(graph);

    return lungeRepository.save(lungeEntity);
  }

  // 특정 사용자의 모든 런지 기록 가져오기
  public List<LungeDTO> getAllLungeByLoginId(String loginId) {
    List<LungeEntity> lungeEntities = lungeRepository.findByLoginId(loginId); // 조회 로직
    return lungeEntities.stream().map(this::convertToLungeDTO).collect(Collectors.toList());
  }

  // DTO로 변환하기
  private LungeDTO convertToLungeDTO(LungeEntity entity) {
    LungeDTO dto = new LungeDTO();
    dto.setExercise_set((entity.getExercise_set()));
    dto.setLogin_id(entity.getUser().getLoginId());
    dto.setTimer_sec(entity.getTimer_sec());
    dto.setCount(entity.getCount());
    dto.setPerfect(entity.getPerfect());
    dto.setGreat(entity.getGreat());
    dto.setGood(entity.getGood());
    dto.setCreated_at(entity.getCreated_at());
    dto.setGraph(entity.getGraph());
    return dto;
  }

  // 런지 기록 그래프 가져오기
  public List<LungeGraphDTO> getLungeGrpah(String loginId) {
    return lungeRepository.findAllByOrderByCreatedAtDesc(loginId);
  }

  // 푸쉬업 기록 저장 로직
  public PushupEntity saveRecordPushup(
      String jwtLoginId,
      Long timerSec,
      int count,
      int perfect,
      int good,
      int great,
      int set,
      LocalDateTime created_at) {

    User user =
        userRepository
            .findByLoginId(jwtLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    PushupEntity pushupEntity = new PushupEntity();
    pushupEntity.setUser(user);
    pushupEntity.setExercise_set(set);
    pushupEntity.setTimer_sec(timerSec);
    pushupEntity.setCount(count);
    pushupEntity.setPerfect(perfect);
    pushupEntity.setGood(good);
    pushupEntity.setGreat(great);
    pushupEntity.setCreated_at(created_at);

    // 성과 지표 계산
    double graph = calculatePerformance(count, timerSec, perfect, great, good);
    pushupEntity.setGraph(graph);

    return pushupRepository.save(pushupEntity);
  }

  // 특정 사용자의 모든 푸쉬업 기록 가져오기
  public List<PushupDTO> getAllPushupByLoginId(String loginId) {
    List<PushupEntity> pushupEntities = pushupRepository.findByLoginId(loginId); // 조회 로직
    return pushupEntities.stream().map(this::convertToPushupDTO).collect(Collectors.toList());
  }

  // DTO로 변경하기
  private PushupDTO convertToPushupDTO(PushupEntity entity) {
    PushupDTO dto = new PushupDTO();
    dto.setLogin_id(entity.getUser().getLoginId());
    dto.setExercise_set(entity.getExercise_set());
    dto.setTimer_sec(entity.getTimer_sec());
    dto.setCount(entity.getCount());
    dto.setPerfect(entity.getPerfect());
    dto.setGreat(entity.getGreat());
    dto.setGood(entity.getGood());
    dto.setCreated_at(entity.getCreated_at());
    dto.setGraph(entity.getGraph());
    return dto;
  }

  // 푸쉬업 기록 그래프 가져오기
  public List<PushupGraphDTO> getPushupGrpah(String loginId) {
    return pushupRepository.findAllByOrderByCreatedAtDesc(loginId);
  }

  // 스쿼트 기록 저장 로직
  public SquatEntity saveRecordSquat(
      String jwtLoginId,
      Long timerSec,
      int count,
      int perfect,
      int good,
      int great,
      LocalDateTime created_at,
      int set) {

    User user =
        userRepository
            .findByLoginId(jwtLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    SquatEntity squatEntity = new SquatEntity();
    squatEntity.setUser(user);
    squatEntity.setExercise_set(set);
    squatEntity.setTimer_sec(timerSec);
    squatEntity.setCount(count);
    squatEntity.setPerfect(perfect);
    squatEntity.setGood(good);
    squatEntity.setGreat(great);
    squatEntity.setCreated_at(created_at);

    // 성과 지표 계산
    double graph = calculatePerformance(count, timerSec, perfect, great, good);
    squatEntity.setGraph(graph);
    return squatRepository.save(squatEntity);
  }

  // 스쿼트 아이디로 기록 조회하기
  public List<SquatDTO> getAllSquatByLoginId(String loginId) {
    List<SquatEntity> squatEntities = squatRepository.findByLoginId(loginId); // 조회 로직
    return squatEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  // DTO로 변경하기
  private SquatDTO convertToDTO(SquatEntity entity) {
    SquatDTO dto = new SquatDTO();
    dto.setLogin_id(entity.getUser().getLoginId());
    dto.setExercise_set(entity.getExercise_set());
    dto.setTimer_sec(entity.getTimer_sec());
    dto.setCount(entity.getCount());
    dto.setPerfect(entity.getPerfect());
    dto.setGreat(entity.getGreat());
    dto.setGood(entity.getGood());
    dto.setCreated_at(entity.getCreated_at());
    dto.setGraph(entity.getGraph());
    return dto;
  }

  // 스쿼트 기록 그래프 가져오기
  public List<SquatGraphDTO> getSquatGrpah(String loginId) {
    return squatRepository.findAllByOrderByCreatedAtDesc(loginId);
  }
}

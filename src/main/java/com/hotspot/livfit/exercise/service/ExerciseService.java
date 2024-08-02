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

  // 런지 기록 저장 로직
  public LungeDTO saveRecordLunge(
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
    lungeRepository.save(lungeEntity);

    return new LungeDTO(
        user.getLoginId(),
        lungeEntity.getTimer_sec(),
        lungeEntity.getCount(),
        lungeEntity.getPerfect(),
        lungeEntity.getGreat(),
        lungeEntity.getGood(),
        lungeEntity.getCreated_at(),
        lungeEntity.getExercise_set());
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
    return dto;
  }

  //런지 운동 기록 그래프 값 가져오기
  public List<LungeGraphDTO> getLungeGraph(String loginId) {
    List<LungeEntity> lungeEntities = lungeRepository.findAllByOrderByCreatedAtDesc(loginId);
    if (lungeEntities.isEmpty()) {
      throw new RuntimeException("No exercises found for user with login ID: " + loginId);
    }

    int totalCounts =
        lungeEntities.stream().mapToInt(LungeEntity::getCount).sum(); // 모든 count 값들의 합산
    long totaltime =
        lungeEntities.stream().mapToLong(LungeEntity::getTimer_sec).sum(); // 모든 timer_sec 값들의 합산

    return lungeEntities.stream()
        .map(
            entity ->
                new LungeGraphDTO(
                    entity.getCreated_at(), totaltime, entity.getCount(), totalCounts))
        .collect(Collectors.toList());
  }

  // 푸쉬업 기록 저장 로직
  public PushupDTO saveRecordPushup(
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

    PushupEntity pushupEntity = new PushupEntity();
    pushupEntity.setExercise_set(set);
    pushupEntity.setUser(user);
    pushupEntity.setTimer_sec(timerSec);
    pushupEntity.setCount(count);
    pushupEntity.setPerfect(perfect);
    pushupEntity.setGood(good);
    pushupEntity.setGreat(great);
    pushupEntity.setCreated_at(created_at);

    pushupRepository.save(pushupEntity);
    return new PushupDTO(
        user.getLoginId(),
        pushupEntity.getTimer_sec(),
        pushupEntity.getCount(),
        pushupEntity.getPerfect(),
        pushupEntity.getGreat(),
        pushupEntity.getGood(),
        pushupEntity.getCreated_at(),
        pushupEntity.getExercise_set());
  }

  // 특정 사용자의 모든 푸쉬업 기록 가져오기
  public List<PushupDTO> getAllPushupByLoginId(String loginId) {
    List<PushupEntity> pushupEntities = pushupRepository.findByLoginId(loginId); // 조회 로직
    return pushupEntities.stream().map(this::convertToPushupDTO).collect(Collectors.toList());
  }

  // DTO로 변환하기
  private PushupDTO convertToPushupDTO(PushupEntity entity) {
    PushupDTO dto = new PushupDTO();
    dto.setExercise_set((entity.getExercise_set()));
    dto.setLogin_id(entity.getUser().getLoginId());
    dto.setTimer_sec(entity.getTimer_sec());
    dto.setCount(entity.getCount());
    dto.setPerfect(entity.getPerfect());
    dto.setGreat(entity.getGreat());
    dto.setGood(entity.getGood());
    dto.setCreated_at(entity.getCreated_at());
    return dto;
  }

  //푸쉬업 운동 기록 그래프 값 가져오기
  public List<PushupGraphDTO> getPushupGraph(String loginId) {
    List<PushupEntity> pushupEntities = pushupRepository.findAllByOrderByCreatedAtDesc(loginId);
    if (pushupEntities.isEmpty()) {
      throw new RuntimeException("No exercises found for user with login ID: " + loginId);
    }

    int totalCounts =
        pushupEntities.stream().mapToInt(PushupEntity::getCount).sum(); // 모든 count 값들의 합산
    long totaltime =
        pushupEntities.stream().mapToLong(PushupEntity::getTimer_sec).sum(); // 모든 timer_sec 값들의 합산

    return pushupEntities.stream()
        .map(
            entity ->
                new PushupGraphDTO(
                    entity.getCreated_at(), totaltime, entity.getCount(), totalCounts))
        .collect(Collectors.toList());
  }

  // 스쿼트 기록 저장 로직
  public SquatDTO saveRecordSquat(
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
    squatEntity.setExercise_set(set);
    squatEntity.setUser(user);
    squatEntity.setTimer_sec(timerSec);
    squatEntity.setCount(count);
    squatEntity.setPerfect(perfect);
    squatEntity.setGood(good);
    squatEntity.setGreat(great);
    squatEntity.setCreated_at(created_at);

    squatRepository.save(squatEntity);
    return new SquatDTO(
        user.getLoginId(),
        squatEntity.getTimer_sec(),
        squatEntity.getCount(),
        squatEntity.getPerfect(),
        squatEntity.getGreat(),
        squatEntity.getGood(),
        squatEntity.getCreated_at(),
        squatEntity.getExercise_set());
  }

  // 특정 사용자의 모든 스쿼트 기록 가져오기
  public List<SquatDTO> getAllSquatByLoginId(String loginId) {
    List<SquatEntity> squatEntities = squatRepository.findByLoginId(loginId); // 조회 로직
    return squatEntities.stream().map(this::convertToSquatDTO).collect(Collectors.toList());
  }

  // DTO로 변환하기
  private SquatDTO convertToSquatDTO(SquatEntity entity) {
    SquatDTO dto = new SquatDTO();
    dto.setExercise_set((entity.getExercise_set()));
    dto.setLogin_id(entity.getUser().getLoginId());
    dto.setTimer_sec(entity.getTimer_sec());
    dto.setCount(entity.getCount());
    dto.setPerfect(entity.getPerfect());
    dto.setGreat(entity.getGreat());
    dto.setGood(entity.getGood());
    dto.setCreated_at(entity.getCreated_at());
    return dto;
  }
  //스쿼트 운동 기록 그래프 값 가져오기
  public List<SquatGraphDTO> getSquatGraph(String loginId) {
    List<SquatEntity> squatEntities = squatRepository.findAllByOrderByCreatedAtDesc(loginId);
    if (squatEntities.isEmpty()) {
      throw new RuntimeException("No exercises found for user with login ID: " + loginId);
    }

    int totalCounts =
        squatEntities.stream().mapToInt(SquatEntity::getCount).sum(); // 모든 count 값들의 합산
    long totaltime =
        squatEntities.stream().mapToLong(SquatEntity::getTimer_sec).sum(); // 모든 timer_sec 값들의 합산

    return squatEntities.stream()
        .map(
            entity ->
                new SquatGraphDTO(
                    entity.getCreated_at(), totaltime, entity.getCount(), totalCounts))
        .collect(Collectors.toList());
  }
}

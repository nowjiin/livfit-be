package com.hotspot.livfit.exercise.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.exercise.dto.LungeGraphDTO;
import com.hotspot.livfit.exercise.dto.PushupGraphDTO;
import com.hotspot.livfit.exercise.dto.SquatGraphDTO;
import com.hotspot.livfit.exercise.entity.LungeEntity;
import com.hotspot.livfit.exercise.entity.PushupEntity;
import com.hotspot.livfit.exercise.entity.SquatEntity;
import com.hotspot.livfit.exercise.repository.LungeRepository;
import com.hotspot.livfit.exercise.repository.PushupRepository;
import com.hotspot.livfit.exercise.repository.SquatRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;
import com.hotspot.livfit.user.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class ExerciseService {
  private final LungeRepository lungeRepository;
  private final PushupRepository pushupRepository;
  private final SquatRepository squatRepository;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  // 런지 기록 저장 로직
  public LungeEntity saveRecordLunge(
      String jwtLoginId, Long timerSec, int count, int perfect, int good, int great) {

    User user =
        userRepository
            .findByLoginId(jwtLoginId) // 이 메소드는 UserRepository에 정의되어야 합니다.
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    LungeEntity lungeEntity = new LungeEntity();
    lungeEntity.setUser(user);
    lungeEntity.setTimer_sec(timerSec);
    lungeEntity.setCount(count);
    lungeEntity.setPerfect(perfect);
    lungeEntity.setGood(good);
    lungeEntity.setGreat(great);

    double efficiency = timerSec > 0 ? (double) count / timerSec : 0.0; // 0으로 나누는 경우를 방지

    // 품질 점수 계산 (가중치 적용)
    int quality = (perfect * 3) + (great * 2) + good;

    // 종합 성과 지표 계산
    double graph = efficiency + quality;

    // 종합 성과 지표 설정
    lungeEntity.setGraph(graph);

    return lungeRepository.save(lungeEntity);
  }

  // 특정 사용자의 모든 런지 기록 가져오기
  public List<LungeEntity> getAllLungeByLoginId(String loginId) {
    return lungeRepository.findByLoginId(loginId);
  }

  // 푸쉬업 기록 그래프 가져오기
  public List<LungeGraphDTO> getLungeGrpah(String loginId) {
    return lungeRepository.findAllByOrderByCreatedAtDesc(loginId);
  }

  // 푸쉬업 기록 저장 로직
  public PushupEntity saveRecordPushup(
      String jwtLoginId, Long timerSec, int count, int perfect, int good, int great) {

    User user =
        userRepository
            .findByLoginId(jwtLoginId) // 이 메소드는 UserRepository에 정의되어야 합니다.
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    PushupEntity pushupEntity = new PushupEntity();
    pushupEntity.setUser(user);
    pushupEntity.setTimer_sec(timerSec);
    pushupEntity.setCount(count);
    pushupEntity.setPerfect(perfect);
    pushupEntity.setGood(good);
    pushupEntity.setGreat(great);

    // 효율성 점수 계산 (운동 개수 / 시간)
    double efficiency = timerSec > 0 ? (double) count / timerSec : 0.0; // 0으로 나누는 경우를 방지

    // 품질 점수 계산 (가중치 적용)
    int quality = (perfect * 3) + (great * 2) + good;

    // 종합 성과 지표 계산
    double graph = efficiency + quality;

    // 종합 성과 지표 설정
    pushupEntity.setGraph(graph);

    return pushupRepository.save(pushupEntity);
  }

  // 특정 사용자의 모든 푸쉬업 기록 가져오기
  public List<PushupEntity> getAllPushupByLoginId(String loginId) {
    return pushupRepository.findByLoginId(loginId);
  }

  // 푸쉬업 기록 그래프 가져오기
  public List<PushupGraphDTO> getPushupGrpah(String loginId) {
    return pushupRepository.findAllByOrderByCreatedAtDesc(loginId);
  }

  // 스쿼트 기록 저장 로직
  public SquatEntity saveRecordSquat(
      String jwtLoginId, Long timerSec, int count, int perfect, int good, int great) {

    User user =
        userRepository
            .findByLoginId(jwtLoginId) // 이 메소드는 UserRepository에 정의되어야 합니다.
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    SquatEntity squatEntity = new SquatEntity();
    squatEntity.setUser(user);
    squatEntity.setTimer_sec(timerSec);
    squatEntity.setCount(count);
    squatEntity.setPerfect(perfect);
    squatEntity.setGood(good);
    squatEntity.setGreat(great);

    // 효율성 점수 계산 (운동 개수 / 시간)
    double efficiency = timerSec > 0 ? (double) count / timerSec : 0.0; // 0으로 나누는 경우를 방지

    // 품질 점수 계산 (가중치 적용)
    int quality = (perfect * 3) + (great * 2) + good;

    // 종합 성과 지표 계산
    double graph = efficiency + quality;

    // 종합 성과 지표 설정
    squatEntity.setGraph(graph);

    return squatRepository.save(squatEntity);
  }

  // 특정 사용자의 모든 스쿼트 기록 가져오기
  public List<SquatEntity> getAllSquatByLoginId(String loginId) {
    return squatRepository.findByLoginId(loginId);
  }

  // 스쿼트 기록 그래프 가져오기
  public List<SquatGraphDTO> getSquatGrpah(String loginId) {
    return squatRepository.findAllByOrderByCreatedAtDesc(loginId);
  }
}

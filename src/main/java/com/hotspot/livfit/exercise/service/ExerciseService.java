package com.hotspot.livfit.exercise.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

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
    return lungeRepository.save(lungeEntity);
  }

  // 특정 사용자의 모든 런지 기록 가져오기
  public List<LungeEntity> getAllLungeByLoginId(String loginId) {
    return lungeRepository.findByLoginId(loginId);
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
    return pushupRepository.save(pushupEntity);
  }

  // 특정 사용자의 모든 푸쉬업 기록 가져오기
  public List<PushupEntity> getAllPushupByLoginId(String loginId) {
    return pushupRepository.findByLoginId(loginId);
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

    return squatRepository.save(squatEntity);
  }

  // 특정 사용자의 모든 스쿼트 기록 가져오기
  public List<SquatEntity> getAllSquatByLoginId(String loginId) {
    return squatRepository.findByLoginId(loginId);
  }
}

package com.hotspot.livfit.exercise.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.exercise.entity.Lunge;
import com.hotspot.livfit.exercise.entity.Pushup;
import com.hotspot.livfit.exercise.entity.Squat;
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
  public Lunge saveRecordLunge(
      String loginId, Long timerSec, int count, int perfect, int good, int great) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Lunge lunge = new Lunge();
    lunge.setUser(user);
    lunge.setTimer_sec(timerSec);
    lunge.setCount(count);
    lunge.setPerfect(perfect);
    lunge.setGood(good);
    lunge.setGreat(great);
    return lungeRepository.save(lunge);
  }

  // 특정 사용자의 모든 런지 기록 가져오기
  public List<Lunge> getAllLungeByLoginId(String loginId) {
    return lungeRepository.findByLoginId(loginId);
  }

  // 푸쉬업 기록 저장 로직
  public Pushup saveRecordPushup(
      String loginId, Long timerSec, int count, int perfect, int good, int great) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Pushup pushup = new Pushup();
    pushup.setUser(user);
    pushup.setTimer_sec(timerSec);
    pushup.setCount(count);
    pushup.setPerfect(perfect);
    pushup.setGood(good);
    pushup.setGreat(great);
    return pushupRepository.save(pushup);
  }

  // 특정 사용자의 모든 푸쉬업 기록 가져오기
  public List<Pushup> getAllPushupByLoginId(String loginId) {
    return pushupRepository.findByLoginId(loginId);
  }

  // 스쿼트 기록 저장 로직
  public Squat saveRecordSquat(
      String loginId, Long timerSec, int count, int perfect, int good, int great) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Squat squat = new Squat();
    squat.setUser(user);
    squat.setTimer_sec(timerSec);
    squat.setCount(count);
    squat.setPerfect(perfect);
    squat.setGood(good);
    squat.setGreat(great);
    return squatRepository.save(squat);
  }

  // 특정 사용자의 모든 스쿼트 기록 가져오기
  public List<Squat> getAllSquatByLoginId(String loginId) {
    return squatRepository.findByLoginId(loginId);
  }
}

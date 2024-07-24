package com.hotspot.livfit.exercise.entity.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.exercise.entity.entity.Lunge;
import com.hotspot.livfit.exercise.entity.entity.Pushup;
import com.hotspot.livfit.exercise.entity.entity.Squat;
import com.hotspot.livfit.exercise.entity.repository.LungeRepository;
import com.hotspot.livfit.exercise.entity.repository.PushupRepository;
import com.hotspot.livfit.exercise.entity.repository.SquatRepository;
import com.hotspot.livfit.user.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class ExerciseService {
  private final JwtUtil jwtUtil;
  private final LungeRepository lungeRepository;

  private final PushupRepository pushupRepository;
  private final SquatRepository squatRepository;

  // 런지 기록 저장 로직
  public Lunge saveRecordLunge(
      String token, String username, Long timerSec, int count, int perfect, int good, int great) {
    // 리프레시 토큰이 있는지 확인
    if (jwtUtil.validateToken(token, username)) {
      // 런지 기록 DB에 저장
      Lunge lunge = new Lunge();
      lunge.setTimer_sec(timerSec);
      lunge.setCount(count);
      lunge.setPerfect(perfect);
      lunge.setGood(good);
      lunge.setGreat(great);
      return lungeRepository.save(lunge);
    }
    // 토큰이 없는 경우
    return null;
  }

  public List<Lunge> getAllLunge() {
    return lungeRepository.findAll();
  }

  // 푸쉬업 기록 저장 로직
  public Pushup saveRecordPushup(
      String token, String username, Long timerSec, int count, int perfect, int good, int great) {
    // 리프레시 토큰이 있는지 확인
    if (jwtUtil.validateToken(token, username)) {
      // 푸쉬업 기록 DB에 저장
      Pushup pushup = new Pushup();
      pushup.setTimer_sec(timerSec);
      pushup.setCount(count);
      pushup.setPerfect(perfect);
      pushup.setGood(good);
      pushup.setGreat(great);
      return pushupRepository.save(pushup);
    }
    // 토큰이 없는 경우
    return null;
  }

  public List<Pushup> getAllPushup() {
    return pushupRepository.findAll();
  }

  // 스쿼트 기록 저장 로직
  public Squat saveRecordSquat(
      String token, String username, Long timerSec, int count, int perfect, int good, int great) {
    // 리프레시 토큰이 있는지 확인
    if (jwtUtil.validateToken(token, username)) {
      // 스쿼트 기록 DB에 저장
      Squat squat = new Squat();
      squat.setTimer_sec(timerSec);
      squat.setCount(count);
      squat.setPerfect(perfect);
      squat.setGood(good);
      squat.setGreat(great);
      return squatRepository.save(squat);
    }
    // 토큰이 없는 경우
    return null;
  }

  public List<Squat> getAllSquat() {
    return squatRepository.findAll();
  }
}

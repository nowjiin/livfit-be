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

@Service
@RequiredArgsConstructor
public class ExerciseService {
  private final LungeRepository lungeRepository;
  private final PushupRepository pushupRepository;
  private final SquatRepository squatRepository;

  // 런지 기록 저장 로직
  public Lunge saveRecordLunge(Long timerSec, int count, int perfect, int good, int great) {
    // 런지 기록 DB에 저장
    Lunge lunge = new Lunge();
    lunge.setTimer_sec(timerSec);
    lunge.setCount(count);
    lunge.setPerfect(perfect);
    lunge.setGood(good);
    lunge.setGreat(great);
    return lungeRepository.save(lunge);
  }

  // 아이디로 런지 기록 전체 조회
  public List<Lunge> getAllLunge(Long userId) {
    return lungeRepository.findByUserId(userId);
  }

  // 푸쉬업 기록 저장 로직
  public Pushup saveRecordPushup(Long timerSec, int count, int perfect, int good, int great) {
    // 푸쉬업 기록 DB에 저장
    Pushup pushup = new Pushup();
    pushup.setTimer_sec(timerSec);
    pushup.setCount(count);
    pushup.setPerfect(perfect);
    pushup.setGood(good);
    pushup.setGreat(great);
    return pushupRepository.save(pushup);
  }

  // 회원의 푸쉬업 기록 조회
  public List<Pushup> getAllPushup(Long userId) {
    return pushupRepository.findByUserId(userId);
  }

  // 스쿼트 기록 저장 로직
  public Squat saveRecordSquat(Long timerSec, int count, int perfect, int good, int great) {
    // 스쿼트 기록 DB에 저장
    Squat squat = new Squat();
    squat.setTimer_sec(timerSec);
    squat.setCount(count);
    squat.setPerfect(perfect);
    squat.setGood(good);
    squat.setGreat(great);
    return squatRepository.save(squat);
  }

  public List<Squat> getAllSquat(Long userId) {
    return squatRepository.findByUserId(userId);
  }
}
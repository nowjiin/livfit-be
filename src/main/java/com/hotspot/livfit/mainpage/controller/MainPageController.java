package com.hotspot.livfit.mainpage.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.livfit.challenge.dto.UserChallengeResponseDTO;
import com.hotspot.livfit.challenge.service.ChallengeService;
import com.hotspot.livfit.mainpage.service.MainPageService;
import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;
import com.hotspot.livfit.today_exercise.dto.WeeklyExerciseStatusDTO;
import com.hotspot.livfit.today_exercise.service.TodayExerciseService;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/mainpage/")
@RequiredArgsConstructor
@Slf4j
public class MainPageController {
  private final JwtUtil jwtUtil;
  private final MainPageService mainPageService;
  private final UserRepository userRepository;
  private final TodayExerciseService todayExerciseService;
  private final ChallengeService challengeService;

  // 일주일 동안의 운동 상태 조회 (달성률? - 메인페이지 하단)
  @Operation(summary = "일주일 운동 상태 조회", description = "사용자의 일주일 운동 상태를 조회")
  @GetMapping("/week-status")
  public ResponseEntity<?> getWeeklyExerciseStatus(
      @RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      List<WeeklyExerciseStatusDTO> weeklyStatus =
          todayExerciseService.getWeeklyExerciseStatus(loginId);

      return ResponseEntity.ok(weeklyStatus);
    } catch (RuntimeException e) {
      log.error("Error retrieving weekly exercise status: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error retrieving weekly exercise status: " + e.getMessage());
    }
  }

  @Operation(summary = "닉네임 조회", description = "사용자 닉네임 조회")
  @GetMapping("/getname")
  public ResponseEntity<?> getNickname(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      User user =
          userRepository
              .findByLoginId(loginId)
              .orElseThrow(() -> new RuntimeException("User not found"));

      return ResponseEntity.ok(user.getNickname());
    } catch (Exception e) {
      log.error(
          "Error during fetching nickname in controller /api/mainpage/nickname: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body("Error retrieving nickname: " + e.getMessage());
    }
  }

  // 오늘의 운동 조회
  @Operation(summary = "오늘의 운동 조회", description = "오늘의 운동을 조회")
  @GetMapping("/getTodayexercise")
  public ResponseEntity<?> getTodayExercise() {
    try {
      // 현재 날짜 기준으로 오늘의 운동을 조회
      LocalDate today = LocalDate.now();
      TodayExerciseDTO todayExerciseDTO = mainPageService.getTodayExercise(today);
      return ResponseEntity.ok(todayExerciseDTO);
    } catch (Exception e) {
      log.error(
          "Error during fetching today exercise in controller /api/mainpage/getTodayexercise: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body("Error retrieving today exercise: " + e.getMessage());
    }
  }

  @Operation(summary = "현재 진행중인 챌린지 조회", description = "사용자가 참여하고 있는 진행중인 챌린지 목록 조회")
  @GetMapping("/getchallenges")
  public ResponseEntity<?> getOngoingChallenges(
      @RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      log.info("진행중인 챌린지를 조회하는 사용자 아이디: {}", loginId);

      // mainPageService를 사용하여 진행중인 챌린지 조회
      List<UserChallengeResponseDTO> ongoingChallenges =
          mainPageService.getOngoingChallenges(loginId);
      return ResponseEntity.ok(ongoingChallenges);
    } catch (Exception e) {
      log.error(
          "Error during fetching ongoing challenges in controller /api/mainpage/ongoing-challenges: {}",
          e.getMessage());
      return ResponseEntity.badRequest()
          .body("Error retrieving ongoing challenges: " + e.getMessage());
    }
  }
}

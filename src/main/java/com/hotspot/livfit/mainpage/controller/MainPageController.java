package com.hotspot.livfit.mainpage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.livfit.mainpage.dto.MainPageDTO;
import com.hotspot.livfit.mainpage.service.MainPageService;
import com.hotspot.livfit.today_exercise.entity.TodayExercise;
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

  @Operation(summary = "일주일 달성률 확인 (???)", description = "달성한 요일 체크표시")
  @GetMapping("/get-achievement-rate")
  public ResponseEntity<?> getMain(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      log.info("메인페이지에 있는, 사용자 아이디: {}", loginId);

      MainPageDTO responses = mainPageService.getMainPageInfo(loginId);
      return ResponseEntity.ok(responses);
    } catch (Exception e) {
      log.error(
          "Error during fetching mainpage info in controller /api/mainpage: {}", e.getMessage());
      return ResponseEntity.badRequest().body("Error retrieving main page info: " + e.getMessage());
    }
  }

  @Operation(summary = "닉네임 조회", description = "사용자 닉네임 조회")
  @GetMapping("/nickname")
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

  @Operation(summary = "오늘의 운동 조회", description = "오늘의 운동을 랜덤으로 1개 조회")
  @GetMapping("/random-today-exercise")
  public ResponseEntity<?> getRandomTodayExercise() {
    try {
      TodayExercise todayExercise = mainPageService.getRandomTodayExercise();
      return ResponseEntity.ok(todayExercise);
    } catch (Exception e) {
      log.error(
          "Error during fetching random today exercise in controller /api/mainpage/random-today-exercise: {}",
          e.getMessage());
      return ResponseEntity.badRequest()
          .body("Error retrieving random today exercise: " + e.getMessage());
    }
  }
}

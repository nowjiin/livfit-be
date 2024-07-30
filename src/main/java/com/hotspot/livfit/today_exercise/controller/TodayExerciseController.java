package com.hotspot.livfit.today_exercise.controller;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;
import com.hotspot.livfit.today_exercise.entity.TodayExercise;
import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;
import com.hotspot.livfit.today_exercise.service.TodayExerciseService;
import com.hotspot.livfit.user.util.JwtUtil;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/today_exercise")
@RequiredArgsConstructor
@Slf4j
public class TodayExerciseController {
  private final TodayExerciseService todayExerciseService;
  private final JwtUtil jwtUtil;
  /*
   * URL: api/today_exercise/show/
   * HTTP Method: GET
   * 토큰 필요 x
   */

  // 오늘의 운동 기록 저장하기
  @PostMapping("/save")
  public ResponseEntity<?> saveTodayExercise(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody TodayExerciseDTO todayExerciseRequest) {

    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Invalid Authorization header format.");
    }
    String token = bearerToken.substring(7).trim();
    if (token.isEmpty() || token.split("\\.").length != 3) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token format.");
    }

    try {
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String jwtLoginId = claims.getId(); // JWT에서 사용자 ID를 추출

      // TodayExerciseService를 통해 오늘의 운동 기록을 저장
      TodayExerciseUser savedExercise =
          todayExerciseService.saveChallenge(
              jwtLoginId, todayExerciseRequest.getDayOfWeek(), todayExerciseRequest.getSuccess());

      return ResponseEntity.ok(savedExercise);
    } catch (Exception e) {
      log.error("An error occurred while saving the today exercise record: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while saving the today exercise record: " + e.getMessage());
    }
  }

  // 번호에 맞춰서 프론트 엔드가 사용하게[오늘의 운동 리스트 아이디]
  @GetMapping("/show/{id}")
  public ResponseEntity<TodayExercise> getTodayExerciseById(@PathVariable Long id) {
    Optional<TodayExercise> todayExercise = todayExerciseService.getTodayExercises(id);
    return todayExercise.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}

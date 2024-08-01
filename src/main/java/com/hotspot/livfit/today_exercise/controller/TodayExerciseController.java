package com.hotspot.livfit.today_exercise.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;
import com.hotspot.livfit.today_exercise.dto.TodayExerciseStatusRequestDTO;
import com.hotspot.livfit.today_exercise.service.TodayExerciseService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/today_exercise")
@RequiredArgsConstructor
@Slf4j
public class TodayExerciseController {
  private final TodayExerciseService todayExerciseService;
  private final JwtUtil jwtUtil;

  // 오늘의 운동 조회 (아이디로)
  @Operation(summary = "ID값으로 운동 조회", description = "운동 ID로 오늘의 운동 정보를 조회")
  @GetMapping("/{exerciseId}")
  public ResponseEntity<?> getTodayExerciseById(@PathVariable Long exerciseId) {
    try {
      TodayExerciseDTO exerciseDTO = todayExerciseService.getTodayExerciseById(exerciseId);
      return ResponseEntity.ok(exerciseDTO);
    } catch (RuntimeException e) {
      log.error("Error retrieving exercise by ID {}: {}", exerciseId, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Exercise not found: " + e.getMessage());
    }
  }

  @Operation(summary = "오늘의 운동 상태 기록", description = "사용자의 오늘의 운동 상태를 기록")
  @PostMapping("/record")
  public ResponseEntity<?> recordExerciseStatus(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody TodayExerciseStatusRequestDTO requestDTO) {
    try {
      // JWT 토큰에서 loginId 추출
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      // 운동 상태 기록
      todayExerciseService.recordExerciseStatus(loginId, requestDTO.getStatus());

      return ResponseEntity.ok("Today's exercise status recorded successfully.");
    } catch (RuntimeException e) {
      log.error("Error recording today's exercise status: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error recording today's exercise status: " + e.getMessage());
    }
  }
}

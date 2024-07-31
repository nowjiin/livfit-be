package com.hotspot.livfit.today_exercise.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.today_exercise.dto.TodayExerciseDTO;
import com.hotspot.livfit.today_exercise.entity.TodayExercise;
import com.hotspot.livfit.today_exercise.entity.TodayExerciseUser;
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
  private static final Logger logger = LoggerFactory.getLogger(TodayExerciseController.class);

  // 오늘의 운동 기록 저장
  /*
   * URL: /api/today_exercise/save
   * HTTP Method: POST
   * HTTP Body: record (JSON 형식)
   * 요청 JSON 형식:
   * {
   *   "login_id": "test_dev", // users의 login_id 참조
   *   "success": "success",
   *   "dayOfWeek": "MONDAY",
   * }
   */

  // 오늘의 운동 기록 저장하기
  @Operation(summary = "오늘의 운동 기록 저장", description = "사용자의 오늘의 운동 기록 저장하기")
  @PostMapping("/save")
  public ResponseEntity<?> saveTodayExercise(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody TodayExerciseDTO todayExerciseRequest) {

    String token = bearerToken.substring(7).trim();
    if (token.isEmpty() || token.split("\\.").length != 3) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token format.");
    }

    try {
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String jwtLoginId = claims.getId(); // JWT에서 사용자 ID를 추출
      DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek(); // 오늘 날짜에서 요일 뽑아오기

      // TodayExerciseService를 통해 오늘의 운동 기록을 저장
      TodayExerciseUser todayExerciseUser =
          todayExerciseService.saveChallenge(
              jwtLoginId, dayOfWeek, todayExerciseRequest.getSuccess());
      System.out.println(dayOfWeek);
      // 로그
      logger.info("일일 운동 기록 저장됨: {}", todayExerciseUser);

      // 기록 저장 성공시 띄울 메시지
      return ResponseEntity.ok().body("오늘의 운동 기록 저장 완료");

    } catch (Exception e) {
      log.error("An error occurred while saving the today exercise record: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while saving the today exercise record: " + e.getMessage());
    }
  }

  /*
   * URL: api/today_exercise/show/id
   * HTTP Method: GET
   * 토큰 필요 x
   */

  // 번호에 맞춰서 프론트 엔드가 사용하게[오늘의 운동 리스트 아이디]
  // ex). id = 1 -> 1번에 저장되어 있는 db 내용 조회 후 전달
  @Operation(summary = "db에 있는 오늘의 운동 조회", description = "db에 있는 오늘의 운동 종류 가져오기")
  @GetMapping("/show/{id}")
  public ResponseEntity<TodayExercise> getTodayExerciseById(@PathVariable Long id) {
    Optional<TodayExercise> todayExercise = todayExerciseService.getTodayExercises(id);
    return todayExercise.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}

package com.hotspot.livfit.exercise.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.dto.PushupDTO;
import com.hotspot.livfit.exercise.dto.PushupGraphDTO;
import com.hotspot.livfit.exercise.dto.RecordDTO;
import com.hotspot.livfit.exercise.repository.PushupRepository;
import com.hotspot.livfit.exercise.service.ExerciseService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("/api/pushup")
@RequiredArgsConstructor
@Slf4j
public class PushupController {
  private final ExerciseService exerciseService;
  private final JwtUtil jwtUtil;
  private final PushupRepository pushupRepository;
  private static final Logger logger = LoggerFactory.getLogger(PushupController.class);

  // 푸쉬업 기록 저장
  /*
  * URL: /api/pushup/save_record
  * HTTP Method: POST
  * HTTP Body: record (JSON 형식)
  * 요청 JSON 형식:
  * {
  *   "login_id": "test_dev", // users의 login_id 참조
  *   "timerSec": "60",
  *   "count": "15",
  *   "perfect": "5",
  *    "great": "5",
  *    "good": "5",

  * }
  */

  @PostMapping("/save_record")
  public ResponseEntity<?> saveRecord(
      @RequestHeader("Authorization") String bearerToken, @RequestBody RecordDTO recordDto) {
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
      String jwtLoginId = claims.getId();

      exerciseService.saveRecordPushup(
          jwtLoginId,
          recordDto.getTimer_sec(),
          recordDto.getCount(),
          recordDto.getPerfect(),
          recordDto.getGood(),
          recordDto.getGreat(),
          recordDto.getSet(),
          recordDto.getCreated_at(),
          recordDto.getGraph());
      logger.info("푸쉬업 기록 저장 사용자 : {}", jwtLoginId);

      return ResponseEntity.ok().body("pushup record saved successfully.");
    } catch (JwtException e) {
      log.error("JWT processing error: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("JWT processing error: " + e.getMessage());
    } catch (Exception e) {
      log.error("Error saving pushup record: {}", e.getMessage(), e);
      return ResponseEntity.badRequest().body("Error saving pushup record: " + e.getMessage());
    }
  }
  /*
   * URL: /api/pushup/get_my_record
   * HTTP Method: GET
   */

  @GetMapping("/get_my_record")
  public ResponseEntity<?> getRecord(@RequestHeader("Authorization") String bearerToken) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // 모든 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 클레임에서 로그인 아이디 추출 -> 로그인 아이디로 사용자 운동 기록 가져오기
      String jwtLoginId = claims.getId();

      // 로그인 아이디로 사용자 운동 가져오기
      logger.info("푸쉬업 기록 조회, 사용자 아이디: {}", jwtLoginId);

      List<PushupDTO> pushuprecords = exerciseService.getAllPushupByLoginId(jwtLoginId);
      return ResponseEntity.ok(pushuprecords);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user squat in controller /api/squats/get_my_record: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * URL: /api/pushup/graph
   * HTTP Method: GET
   */
  @GetMapping("/graph")
  public ResponseEntity<?> getGraph(@RequestHeader("Authorization") String bearerToken) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // 모든 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 클레임에서 로그인 아이디 추출 -> 로그인 아이디로 사용자 운동 기록 가져오기
      String jwtLoginId = claims.getId();
      logger.info("푸쉬업 그래프 기록 조회, 사용자 아이디: {}", jwtLoginId);

      List<PushupGraphDTO> pushupEntities = exerciseService.getPushupGrpah(jwtLoginId);
      return ResponseEntity.ok(pushupEntities);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user pushup in controller /api/pushup/graph: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

package com.hotspot.livfit.exercise.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.dto.PushupDTO;
import com.hotspot.livfit.exercise.dto.PushupGraphDTO;
import com.hotspot.livfit.exercise.dto.RecordDTO;
import com.hotspot.livfit.exercise.service.ExerciseService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/pushup")
@RequiredArgsConstructor
@Slf4j
public class PushupController {
  private final ExerciseService exerciseService;
  private final JwtUtil jwtUtil;

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
  @Operation(summary = "푸쉬업 운동 기록 저장", description = "푸쉬업 운동 후 개인 기록 저장")
  @PostMapping("/save_record")
  public ResponseEntity<?> saveRecord(
      @RequestHeader("Authorization") String bearerToken, @RequestBody RecordDTO recordDto) {

    String token = bearerToken.substring(7).trim();
    if (token.isEmpty() || token.split("\\.").length != 3) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token format.");
    }

    try {
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 클레임에서 로그인 아이디 추출 -> 로그인 아이디로 사용자 운동 기록 가져오기
      String jwtLoginId = claims.getId();

      // 기록 저장하기
      PushupDTO pushupDTO =
          exerciseService.saveRecordPushup(
              jwtLoginId,
              recordDto.getTimer_sec(),
              recordDto.getCount(),
              recordDto.getPerfect(),
              recordDto.getGood(),
              recordDto.getGreat(),
              recordDto.getCreated_at(),
              recordDto.getExercise_set());

      log.info("푸쉬업 기록 저장 사용자 : {}", jwtLoginId);
      // 기록이 저장될 때 띄울 메시지
      return ResponseEntity.ok(pushupDTO);

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

  @Operation(summary = "사용자의 푸쉬업 운동 기록 가져오기", description = "로그인한 사용자의 개별 운동기록 가져오기")
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
      log.info("푸쉬업 기록 조회, 사용자 아이디: {}", jwtLoginId);

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
  @Operation(summary = "사용자의 운동(푸쉬업) 그래프 기록 가져오기", description = "그래프 안의 값 가져오기")
  @GetMapping("/graph")
  public ResponseEntity<?> getGraph(@RequestHeader("Authorization") String bearerToken) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // 모든 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 클레임에서 로그인 아이디 추출 -> 로그인 아이디로 사용자 운동 기록 가져오기
      String jwtLoginId = claims.getId();
      log.info("푸쉬업 그래프 기록 조회, 사용자 아이디: {}", jwtLoginId);

      List<PushupGraphDTO> pushupEntities = exerciseService.getPushupGraph(jwtLoginId);
      return ResponseEntity.ok(pushupEntities);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user pushup in controller /api/pushup/graph: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

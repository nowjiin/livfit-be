package com.hotspot.livfit.exercise.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.dto.Record;
import com.hotspot.livfit.exercise.entity.Pushup;
import com.hotspot.livfit.exercise.service.ExerciseService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

  @Operation(summary = "기록저장", description = "푸쉬업 기록 저장")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "기록 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })
  @PostMapping("/save_record")
  public ResponseEntity<?> saveRecord(
      @RequestHeader("Authorization") String bearerToken, @RequestBody Record record) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // JWT에서 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 토큰의 정보 중에서 로그인 아이디 추출
      String extractedLoginId = claims.getId(); // 일반적으로 로그인 아이디는 subject에 저장됩니다

      // 추출한 로그인 아이디와 요청의 로그인 아이디가 일치하는지 확인
      if (!extractedLoginId.equals(record.getLoginId())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Login ID does not match with the token");
      }

      Pushup pushup =
          exerciseService.saveRecordPushup(
              record.getLoginId(),
              record.getTimer_sec(),
              record.getCount(),
              record.getPerfect(),
              record.getGreat(),
              record.getGood());
      return ResponseEntity.ok(pushup);
    } catch (RuntimeException e) {
      log.error(
          "Error during saving Pushup record in controller /api/pushup/save_record: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  /*
   * URL: /api/pushup/get_my_record
   * HTTP Method: GET
   */
  @Operation(summary = "기록 가져오기", description = "푸쉬업 기록 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "가져오기 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })
  @GetMapping("/get_my_record")
  public ResponseEntity<List<Pushup>> getAllRecords(
      @RequestHeader("Authorization") String bearerToken) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // 모든 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 클레임에서 로그인 아이디 추출 -> 로그인 아이디로 사용자 푸쉬업 기록 가져오기
      String jwtLoginId = claims.getId();

      // 로그인 아이디로 사용자 푸쉬업 기록 조회
      List<Pushup> pushups = exerciseService.getAllPushupByLoginId(jwtLoginId);
      return ResponseEntity.ok(pushups);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching Pushup records in controller /api/pushup/get_my_record: {}",
          e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}

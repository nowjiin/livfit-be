package com.hotspot.livfit.exercise.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.dto.RecordDTO;
import com.hotspot.livfit.exercise.entity.LungeEntity;
import com.hotspot.livfit.exercise.service.ExerciseService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/lunge")
@RequiredArgsConstructor
@Slf4j
public class LungeController {
  private final ExerciseService exerciseService;
  private final JwtUtil jwtUtil;

  // 런지 기록 저장
  /*
  * URL: /api/lunge/save_record
  * HTTP Method: POST
  * HTTP Body: record (JSON 형식)
  * 요청 JSON 형식:
  * {
  *   "timerSec": "60",
  *   "count": "15",
  *   "perfect": "5",
  *    "great": "5",
  *    "good": "5",

  * }
  */

  @Operation(summary = "기록저장", description = "런지 기록 저장")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "기록 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })
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

      exerciseService.saveRecordLunge(
          jwtLoginId,
          recordDto.getTimer_sec(),
          recordDto.getCount(),
          recordDto.getPerfect(),
          recordDto.getGood(),
          recordDto.getGreat());

      return ResponseEntity.ok().body("lunge record saved successfully.");
    } catch (JwtException e) {
      log.error("JWT processing error: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("JWT processing error: " + e.getMessage());
    } catch (Exception e) {
      log.error("Error saving lunge record: {}", e.getMessage(), e);
      return ResponseEntity.badRequest().body("Error saving lunge record: " + e.getMessage());
    }
  }
  /*
   * URL: /api/lunge/get_my_record
   * HTTP Method: GET
   */
  @Operation(summary = "기록 가져오기", description = "런지 기록 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "가져오기 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })
  @GetMapping("/get_my_record")
  public ResponseEntity<List<LungeEntity>> getAllRecords(
      @RequestHeader("Authorization") String bearerToken) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // 모든 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 클레임에서 로그인 아이디 추출 -> 로그인 아이디로 사용자 뱃지 가져오기
      String jwtLoginId = claims.getId();

      // 로그인 아이디로 사용자 런지 기록 조회
      List<LungeEntity> lungeEntities = exerciseService.getAllLungeByLoginId(jwtLoginId);
      return ResponseEntity.ok(lungeEntities);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching Lunge records in controller /api/lunge/get_my_record: {}",
          e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}

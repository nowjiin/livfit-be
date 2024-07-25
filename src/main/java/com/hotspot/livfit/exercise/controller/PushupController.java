package com.hotspot.livfit.exercise.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.dto.Record;
import com.hotspot.livfit.exercise.entity.Pushup;
import com.hotspot.livfit.exercise.service.ExerciseService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/pushup") // 엔드포인트에 "/api" 추가
@RequiredArgsConstructor
@Slf4j
public class PushupController {
  private final ExerciseService exerciseService;
  private final JwtUtil jwtUtil;

  // 푸쉬업 기록 저장 엔드포인트
  /*
   * URL : /api/pushup/{user_id}/record
   * HTTP Method: POST
   * 요청 JSON 형식 :
   * {
   *   "timer_sec" : "60",
   *   "count" : "15",
   *   "perfect" : "5",
   *   "great" : "5",
   *   "good" : "5"
   * }
   * */
  @Operation(summary = "기록저장", description = "푸쉬업 기록 저장")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "기록 완료."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청."),
        @ApiResponse(responseCode = "500", description = "서버 에러.")
      })
  @PostMapping("/{user_id}/record")
  public ResponseEntity<?> saveRecord(
      @RequestHeader("Authorization") String bearerToken, @RequestBody Record record) {
    try {
      String token = bearerToken.substring(7); // "Bearer " 부분 제거
      // JWT에서 사용자 ID 추출
      String userId = jwtUtil.extractUsername(token);
      Pushup pushup =
          exerciseService.saveRecordPushup(
              record.getTimer_sec(),
              record.getCount(),
              record.getPerfect(),
              record.getGreat(),
              record.getGood());
      return ResponseEntity.ok(pushup);

    } catch (RuntimeException e) {
      log.error(
          "Error during saving Pushup record in controller /api/{user_id}/pushup/record: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 푸쉬업 기록 가져오는 엔드포인트
  /*
   * URL : /api/pushup/{user_id}/all
   * HTTP Method: GET
   * */
  @Operation(summary = "기록 가져오기", description = "푸쉬업 기록 조회")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = " 가져오기 완료."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청."),
        @ApiResponse(responseCode = "500", description = "서버 에러.")
      })
  @GetMapping("/{user_id}/all")
  public ResponseEntity<List<Pushup>> getAllRecords(Long user_id) {
    try {
      List<Pushup> pushups = exerciseService.getAllPushup(user_id);
      return ResponseEntity.ok(pushups);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching pushup records in controller /pushup/{user_id}/all: {}",
          e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}

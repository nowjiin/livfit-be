package com.hotspot.livfit.exercise.entity.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.entity.dto.Record;
import com.hotspot.livfit.exercise.entity.entity.Pushup;
import com.hotspot.livfit.exercise.entity.service.ExerciseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/pushup")
@RequiredArgsConstructor
@Slf4j
public class PushupController {
  private final ExerciseService exerciseService;
  // 푸쉬업 기록 저장 엔드포인트
  /*
   * URL : /api/pushup/record
   * HTTP Method: POST
   * 요청 JSON 형식 :
   * {
   *   "username" : "test1",
   *   "token" : "??????",
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
  @PostMapping("/record")
  public ResponseEntity<?> saveRecord(@RequestBody Record record) {
    try {
      Pushup pushup =
          exerciseService.saveRecordPushup(
              record.getToken(),
              record.getUsername(),
              record.getTimer_sec(),
              record.getCount(),
              record.getPerfect(),
              record.getGreat(),
              record.getGood());
      return ResponseEntity.ok(pushup);

    } catch (RuntimeException e) {
      log.error(
          "Error during saving Pushup record in controller /api/pushup/record: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 푸쉬업 기록 가져오는 엔드포인트
  /*
   * URL : /api/pushup/all
   * HTTP Method: GET
   * */
  @Operation(summary = "기록 가져오기", description = "푸쉬업 기록 조회")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = " 가져오기 완료."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청."),
        @ApiResponse(responseCode = "500", description = "서버 에러.")
      })
  @GetMapping("/all")
  public ResponseEntity<List<Pushup>> getAllRecords() {
    try {
      List<Pushup> pushups = exerciseService.getAllPushup();
      return ResponseEntity.ok(pushups);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching pushup records in controller /pushup/all: {}", e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}

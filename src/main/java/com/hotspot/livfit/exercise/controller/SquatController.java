package com.hotspot.livfit.exercise.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.dto.Record;
import com.hotspot.livfit.exercise.entity.Squat;
import com.hotspot.livfit.exercise.service.ExerciseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/squat")
@RequiredArgsConstructor
@Slf4j
public class SquatController {
  private final ExerciseService exerciseService;
  // 스쿼트 기록 저장 엔드포인트
  /*
   * URL : /api/squat/record
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
      Squat squat =
          exerciseService.saveRecordSquat(
              record.getToken(),
              record.getUsername(),
              record.getTimer_sec(),
              record.getCount(),
              record.getPerfect(),
              record.getGreat(),
              record.getGood());
      return ResponseEntity.ok(squat);

    } catch (RuntimeException e) {
      log.error(
          "Error during saving squat record in controller /api/squat/record: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 스쿼트 기록 가져오는 엔드포인트
  /*
   * URL : /api/squat/all
   * HTTP Method: GET
   * */
  @Operation(summary = "기록 가져오기", description = "스쿼트 기록 조회")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = " 가져오기 완료."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청."),
        @ApiResponse(responseCode = "500", description = "서버 에러.")
      })
  @GetMapping("/all")
  public ResponseEntity<List<Squat>> getAllRecords() {
    try {
      List<Squat> squats = exerciseService.getAllSquat();
      return ResponseEntity.ok(squats);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching pushup records in controller /squat/all: {}", e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}

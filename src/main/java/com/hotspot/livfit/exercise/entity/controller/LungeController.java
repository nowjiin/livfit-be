package com.hotspot.livfit.exercise.entity.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.entity.dto.Record;
import com.hotspot.livfit.exercise.entity.entity.Lunge;
import com.hotspot.livfit.exercise.entity.service.ExerciseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/lunge")
@RequiredArgsConstructor
@Slf4j
public class LungeController {
  private final ExerciseService exerciseService;
  // 런지 기록 저장 엔드포인트
  /*
   * URL : /api/lunge/record
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
  @Operation(summary = "기록저장", description = "런지 기록 저장")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "기록 완료."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청."),
        @ApiResponse(responseCode = "500", description = "서버 에러.")
      })
  @PostMapping("/record")
  public ResponseEntity<?> saveRecord(@RequestBody Record record) {
    try {
      Lunge lunge =
          exerciseService.saveRecordLunge(
              record.getToken(),
              record.getUsername(),
              record.getTimer_sec(),
              record.getCount(),
              record.getPerfect(),
              record.getGreat(),
              record.getGood());
      return ResponseEntity.ok(lunge);

    } catch (RuntimeException e) {
      log.error(
          "Error during saving Lunge record in controller /api/lunge/record: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 런지 기록 가져오는 엔드포인트
  /*
   * URL : /api/lunge/all
   * HTTP Method: GET
   * */
  @Operation(summary = "기록 가져오기", description = "런지 기록 조회")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = " 가져오기 완료."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청."),
        @ApiResponse(responseCode = "500", description = "서버 에러.")
      })
  @GetMapping("/all")
  public ResponseEntity<List<Lunge>> getAllRecords() {
    try {
      List<Lunge> lunges = exerciseService.getAllLunge();
      return ResponseEntity.ok(lunges);
    } catch (RuntimeException e) {
      log.error("Error during fetching Lunge records in controller /lunge/all: {}", e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}
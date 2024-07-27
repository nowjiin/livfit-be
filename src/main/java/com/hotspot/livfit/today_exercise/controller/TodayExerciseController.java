package com.hotspot.livfit.today_exercise.controller;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.livfit.today_exercise.entity.TodayExercise;
import com.hotspot.livfit.today_exercise.service.TodayExerciseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/today_exercise")
@RequiredArgsConstructor
@Slf4j
public class TodayExerciseController {
  private final TodayExerciseService todayExerciseService;
  /*
   * URL: api/today_exercise/show/
   * HTTP Method: GET
   * 토큰 필요 x
   */

  @Operation(summary = "오늘의 운동 id별 리스트", description = "오늘의 운동 가져오기")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "가져오기 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })

  // 번호에 맞춰서 프론트 엔드가 사용하게
  @GetMapping("/show/{id}")
  public ResponseEntity<TodayExercise> getTodayExerciseById(@PathVariable Long id) {
    Optional<TodayExercise> todayExercise = todayExerciseService.getTodayExercises(id);
    return todayExercise.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}

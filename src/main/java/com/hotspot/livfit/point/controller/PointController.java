package com.hotspot.livfit.point.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.point.dto.PointRequestDTO;
import com.hotspot.livfit.point.entity.PointHistory;
import com.hotspot.livfit.point.service.PointService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor // 필드 주입을 위한 생성자 자동 생성
@Slf4j
public class PointController {

  private final PointService pointService;
  private final JwtUtil jwtUtil;

  @Operation(summary = "포인트 적립/차감", description = "사용자의 포인트를 적립하거나 차감")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "포인트 적립/차감 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  @PostMapping("/update")
  public ResponseEntity<?> updatePoints(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody PointRequestDTO pointRequestDTO) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId(); // 로그인 아이디
      // 포인트 적립/차감
      pointService.updatePoints(loginId, pointRequestDTO);
      return ResponseEntity.ok("Points updated successfully");
    } catch (RuntimeException e) {
      log.error(
          "Error during updating points in controller /api/points/update: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "포인트 히스토리 조회", description = "사용자의 포인트 히스토리를 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "포인트 히스토리 조회 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  @GetMapping
  public ResponseEntity<?> getPointHistory(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();
      // 포인트 히스토리 조회
      List<PointHistory> history = pointService.getPointHistory(loginId);
      return ResponseEntity.ok(history);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching point history in controller /api/points: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "누적 포인트 조회", description = "사용자의 누적 포인트를 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "누적 포인트 조회 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  @GetMapping("/total")
  public ResponseEntity<?> getTotalPoints(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();
      // 누적 포인트 조회
      int totalPoints = pointService.getTotalPoints(loginId);
      return ResponseEntity.ok(totalPoints);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching total points in controller /api/points/total: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

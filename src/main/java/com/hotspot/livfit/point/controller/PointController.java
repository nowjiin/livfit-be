package com.hotspot.livfit.point.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.point.dto.PointHistoryDTO;
import com.hotspot.livfit.point.dto.PointRequestDTO;
import com.hotspot.livfit.point.service.PointService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor // 필드 주입을 위한 생성자 자동 생성
@Slf4j
public class PointController {

  private final PointService pointService;
  private final JwtUtil jwtUtil;

  @Operation(summary = "포인트 적립/차감", description = "사용자의 포인트를 적립하거나 차감")
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
  @GetMapping
  public ResponseEntity<?> getPointHistory(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();
      // 포인트 히스토리 조회
      List<PointHistoryDTO> history = pointService.getPointHistory(loginId);
      return ResponseEntity.ok(history);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching point history in controller /api/points: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  // 자동으로 포인트 적립
  @Operation(summary = "운동 완료 시 포인트 적립", description = "사용자가 운동을 완료했을 때 포인트를 적립")
  @PostMapping("/earn-points")
  public ResponseEntity<?> earnPoints(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId(); // 로그인 아이디
      // 포인트 적립
      PointRequestDTO pointRequestDTO = new PointRequestDTO();
      pointRequestDTO.setPoints(300);
      pointRequestDTO.setType("earn");
      pointRequestDTO.setDescription("운동 완료");
      pointService.updatePoints(loginId, pointRequestDTO);
      return ResponseEntity.ok("Points earned successfully");
    } catch (RuntimeException e) {
      log.error(
          "Error during earning points in controller /api/points/earn-points: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

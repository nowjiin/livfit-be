package com.hotspot.livfit.mypage.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.badge.dto.MainBadgeRequestDTO;
import com.hotspot.livfit.badge.service.UserBadgeService;
import com.hotspot.livfit.challenge.dto.UserChallengeResponseDTO;
import com.hotspot.livfit.mypage.service.MyPageService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

  private final MyPageService myPageService;
  private final UserBadgeService userBadgeService;
  private final JwtUtil jwtUtil;

  // 누적 포인트 조회
  @Operation(summary = "누적 포인트 조회", description = "사용자의 누적 포인트를 조회")
  @GetMapping("/points")
  public ResponseEntity<?> getTotalPoints(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      int totalPoints = myPageService.getTotalPoints(loginId);
      return ResponseEntity.ok(totalPoints);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching total points in controller /api/mypage/points: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 뱃지 개수 조회
  @Operation(summary = "뱃지 개수 조회", description = "사용자의 뱃지 개수를 조회")
  @GetMapping("/badges/count")
  public ResponseEntity<?> getBadgeCount(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      int badgeCount = myPageService.getBadgeCount(loginId);
      return ResponseEntity.ok(badgeCount);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching badge count in controller /api/mypage/badges/count: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 메인 뱃지 조회
  @Operation(summary = "메인 뱃지 조회", description = "사용자의 메인 뱃지를 조회")
  @GetMapping("/badges/main")
  public ResponseEntity<?> getMainBadgeId(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      String mainBadgeId = myPageService.getMainBadgeId(loginId);
      return ResponseEntity.ok(mainBadgeId);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching main badge in controller /api/mypage/badges/main: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "메인 뱃지 설정", description = "사용자가 메인 뱃지를 설정")
  @PostMapping("/set-main-badge")
  public ResponseEntity<?> setMainBadge(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody MainBadgeRequestDTO mainBadgeRequestDTO) {

    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String jwtLoginId = claims.getId();

      userBadgeService.setMainBadge(jwtLoginId, mainBadgeRequestDTO.getBadgeId());
      log.info("{} 사용자의 메인뱃지를 {}로 설정 성공.", jwtLoginId, mainBadgeRequestDTO.getBadgeId());
      return ResponseEntity.ok("Main badge set successfully from MyPage");
    } catch (RuntimeException e) {
      log.error(
          "Error during setting main badge in MyPage controller /api/mypage/set-main-badge: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 사용자의 모든 챌린지 기록 조회
  @Operation(summary = "사용자의 모든 챌린지 기록 조회", description = "사용자가 참여한 모든 챌린지 기록을 조회")
  @GetMapping("/challenges")
  public ResponseEntity<?> getUserChallenges(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      List<UserChallengeResponseDTO> userChallenges = myPageService.getUserChallenges(loginId);
      return ResponseEntity.ok(userChallenges);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user challenges in controller /api/mypage/challenges: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

package com.hotspot.livfit.challenge.controller;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.challenge.dto.*;
import com.hotspot.livfit.challenge.repository.ChallengeRepository;
import com.hotspot.livfit.challenge.repository.UserChallengeStatusRepository;
import com.hotspot.livfit.challenge.service.ChallengeService;
import com.hotspot.livfit.user.repository.UserRepository;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/challenge")
@RequiredArgsConstructor
@Slf4j
public class ChallengeController {

  private final ChallengeService challengeService;
  private final JwtUtil jwtUtil;

  // 모든 챌린지 조회
  @Operation(summary = "전체 챌린지 조회", description = "모든 챌린지 목록 조회")
  @GetMapping("/list")
  public ResponseEntity<List<ChallengeSummaryDTO>> getAllChallenges() {
    List<ChallengeSummaryDTO> challengeList = challengeService.findAllChallenges();
    return ResponseEntity.ok(challengeList);
  }

  // ID로 특정 챌린지 조회
  @Operation(summary = "ID로 챌린지 조회", description = "특정 챌린지의 상세 정보 조회")
  @GetMapping("/detail/{id}")
  public ResponseEntity<ChallengeDetailDTO> getChallenge(@PathVariable Long id) {
    Optional<ChallengeDetailDTO> challenge = challengeService.getChallenge(id);
    return challenge.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // 사용자의 챌린지 기록 조회
  @Operation(summary = "사용자 챌린지 기록 조회", description = "사용자의 모든 챌린지 기록 조회")
  @GetMapping("/user")
  public ResponseEntity<List<UserChallengeResponseDTO>> getUserChallenges(
      @RequestHeader("Authorization") String bearerToken) {

    String token = bearerToken.substring(7);
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    String loginId = claims.getId();

    List<UserChallengeResponseDTO> userChallenges = challengeService.getUserChallenges(loginId);
    return ResponseEntity.ok(userChallenges);
  }

  // 챌린지 상태 업데이트 (성공/실패 여부)
  @Operation(summary = "챌린지 상태 업데이트", description = "사용자의 챌린지 상태를 업데이트 (성공/실패 여부)")
  @PutMapping("/update-status")
  public ResponseEntity<?> updateChallengeStatus(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody UserChallengeUpdateRequestDTO dto) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      boolean updated = challengeService.updateChallengeStatus(loginId, dto);
      if (updated) {
        return ResponseEntity.ok("Challenge status updated successfully.");
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Challenge status update failed.");
      }
    } catch (Exception e) {
      log.error("Error during updating challenge status: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error updating challenge status: " + e.getMessage());
    }
  }

  // 챌린지 참여하기
  @Operation(summary = "챌린지 참여", description = "사용자가 특정 챌린지에 참여")
  @PostMapping("/participate")
  public ResponseEntity<?> participateInChallenge(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody UserChallengeRequestDTO dto) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      // 서비스 메서드 호출
      String result = challengeService.participateInChallenge(loginId, dto.getChallengeId());

      if ("Already participating in this challenge.".equals(result)) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.error("Error during participating in challenge: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error participating in challenge: " + e.getMessage());
    }
  }

  // 진행중인 챌린지 (status : 0)
  @Operation(summary = "진행중인 챌린지 조회", description = "사용자가 진행중인 모든 챌린지 조회 (status가 0인 챌린지만 조회)")
  @GetMapping("/in-progress")
  public ResponseEntity<?> getInProgressChallenges(
      @RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      List<UserChallengeResponseDTO> inProgressChallenges =
          challengeService.getInProgressChallenges(loginId);

      if (inProgressChallenges.isEmpty()) {
        log.info("No in-progress challenges found for user: {}", loginId);
        return ResponseEntity.ok("No in-progress challenges found.");
      } else {
        log.info(
            "Retrieved {} in-progress challenges for user: {}",
            inProgressChallenges.size(),
            loginId);
        return ResponseEntity.ok(inProgressChallenges);
      }
    } catch (Exception e) {
      log.error("Error fetching in-progress challenges: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error fetching in-progress challenges: " + e.getMessage());
    }
  }
}

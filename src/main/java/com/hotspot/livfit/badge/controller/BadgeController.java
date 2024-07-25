package com.hotspot.livfit.badge.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.badge.dto.BadgeRequestDTO;
import com.hotspot.livfit.badge.dto.BadgeResponseDTO;
import com.hotspot.livfit.badge.entity.UserBadge;
import com.hotspot.livfit.badge.service.UserBadgeService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/userbadges")
@RequiredArgsConstructor
@Slf4j
public class BadgeController {

  private final UserBadgeService userBadgeService;
  private final JwtUtil jwtUtil;

  // 뱃지 조건 확인 및 부여 엔드포인트
  /*
   * URL: /api/userbadges/check-award
   * HTTP Method: POST
   * HTTP Body: BadgeRequestDTO 객체 (JSON 형식)
   * 요청 JSON 형식:
   * {
   *   "userId": 1, // users의 PK 참조
   *   "badgeId": "T",
   *   "conditionCheck": true
   * }
   */

  @Operation(summary = "뱃지 조건 확인 및 부여", description = "사용자가 특정 뱃지 조건을 만족하면 뱃지를 부여")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "뱃지 부여 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
      })
  @PostMapping("/check-award")
  public ResponseEntity<?> checkandAward( // checkNaward -> checkandAward로 변경
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody BadgeRequestDTO badgeRequestDTO) {

    try {
      // 토큰확인 추가
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // JWT에서 사용자 ID 추출
      String userId = jwtUtil.extractUsername(token);

      // userId와 badgeRequestDTO의 userId가 일치하는지 확인
      if (!userId.equals(badgeRequestDTO.getUserId().toString())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("User ID does not match with the token");
      }

      boolean success =
          userBadgeService.checkandAwardBadge( // checkNaward -> checkandAward로 변경
              badgeRequestDTO.getUserId(),
              badgeRequestDTO.getBadgeId(),
              badgeRequestDTO.isConditionCheck());

      if (success) {
        return ResponseEntity.ok(new BadgeResponseDTO(true, "Badge awarded successfully"));
      } else {
        return ResponseEntity.ok(new BadgeResponseDTO(false, "Badge awarded failed"));
      }
    } catch (RuntimeException e) {
      log.error(
          "Error during badge award in controller /api/userbadges/check-award: {}",
          e.getMessage()); // 유저 컨트롤러 복붙했어서 오타난거 수정..
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  //조회 로직 추가
  @Operation(summary = "사용자의 모든 뱃지 가져오기", description = "특정 사용자의 모든 뱃지 가져오기")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "뱃지 가져오기 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
      })
  @GetMapping("/{userId}")
  public ResponseEntity<?> getUserBadges(
      @RequestHeader("Authorization") String bearerToken, @PathVariable Long userId) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // JWT에서 사용자 ID 추출
      String jwtUserId = jwtUtil.extractUsername(token);

      // 요청된 userId와 JWT에서 추출한 userId가 일치하는지 확인
      if (!jwtUserId.equals(userId.toString())) {
        return ResponseEntity.badRequest().body("User ID does not match with the token");
      }

      // userId로 사용자 뱃지 조회
      List<UserBadge> userBadges = userBadgeService.getUserBadges(userId);
      return ResponseEntity.ok(userBadges);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user badges in controller /api/userbadges/{userId}: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

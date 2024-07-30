package com.hotspot.livfit.badge.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.badge.dto.BadgeRequestDTO;
import com.hotspot.livfit.badge.dto.BadgeResponseDTO;
import com.hotspot.livfit.badge.dto.MainBadgeRequestDTO;
import com.hotspot.livfit.badge.dto.UserBadgeResponseDTO;
import com.hotspot.livfit.badge.entity.UserBadge;
import com.hotspot.livfit.badge.service.UserBadgeService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

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
   *   "userId": "hyuneun", // users의 login_id 참조
   *   "badgeId": "test_badge",
   *   "conditionCheck": true
   * }
   */

  @Operation(summary = "뱃지 조건 확인 및 부여", description = "사용자가 특정 뱃지 조건을 만족하면 뱃지를 부여")
  @PostMapping("/check-award")
  public ResponseEntity<?> checkandAward( // checkNaward -> checkandAward로 변경
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody BadgeRequestDTO badgeRequestDTO) {

    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      // 중복된 뱃지인지 확인하고, 중복되지 않으면 부여
      boolean success =
          userBadgeService.checkandAwardBadge(
              loginId, badgeRequestDTO.getBadgeId(), badgeRequestDTO.isConditionCheck());

      if (success) {
        return ResponseEntity.ok(new BadgeResponseDTO(true, "Badge awarded successfully"));
      } else {
        return ResponseEntity.ok(
            new BadgeResponseDTO(false, "Badge awarded failed or already exists"));
      }
    } catch (RuntimeException e) {
      log.error(
          "Error during badge award in controller /api/userbadges/check-award: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 사용자의 모든 뱃지 가져오기
  @Operation(summary = "사용자의 모든 뱃지 가져오기", description = "특정 사용자의 모든 뱃지 가져오기")
  @GetMapping("/mybadge")
  public ResponseEntity<?> getUserBadges(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String jwtLoginId = claims.getId();

      // 로그인 아이디로 사용자 뱃지 조회
      List<UserBadge> userBadges = userBadgeService.getUserBadges(jwtLoginId);

      List<UserBadgeResponseDTO> badgeResponseDTOS = new ArrayList<>();
      for (UserBadge userBadge : userBadges) {
        UserBadgeResponseDTO dto =
            new UserBadgeResponseDTO(
                userBadge.getId(),
                userBadge.getUser().getLoginId(),
                userBadge.getUser().getNickname(),
                userBadge.getBadge().getId(),
                userBadge.getEarnedTime().toString(),
                userBadge.isMainBadge() // 메인 뱃지 여부
                );
        badgeResponseDTOS.add(dto);
      }
      return ResponseEntity.ok(badgeResponseDTOS);

    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user badges in controller /api/userbadges: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 메인 뱃지 설정
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
      return ResponseEntity.ok("Main badge set successfully");
    } catch (RuntimeException e) {
      log.error(
          "Error during setting main badge in controller /api/userbadges/set-main-badge: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

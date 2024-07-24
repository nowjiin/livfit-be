package com.hotspot.livfit.badge.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.livfit.badge.dto.BadgeRequestDTO;
import com.hotspot.livfit.badge.dto.BadgeResponseDTO;
import com.hotspot.livfit.badge.service.UserBadgeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/userbadges")
@RequiredArgsConstructor
@Slf4j
public class BadgeController {

  private final UserBadgeService userBadgeService;

  // 뱃지 조건 확인 및 부여 엔드포인트
  /*
   * URL: /api/userbadges/check-award
   * HTTP Method: POST
   * HTTP Body: BadgeRequestDTO 객체 (JSON 형식)
   * 요청 JSON 형식:
   * {
   *   "userId": 1,
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
  public ResponseEntity<?> checkNaward(@RequestBody BadgeRequestDTO badgeRequestDTO) {
    try {
      boolean success =
          userBadgeService.checkNawardBadge(
              badgeRequestDTO.getUserId(),
              badgeRequestDTO.getBadgeId(),
              badgeRequestDTO.isConditionCheck());

      if (success) {
        return ResponseEntity.ok(new BadgeResponseDTO(true, "Badge awarded successfully"));
      } else {
        return ResponseEntity.ok(new BadgeResponseDTO(false, "Badge awarded failed"));
      }
    } catch (RuntimeException e) {
      log.error("Error during badge award in controller /api/register: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

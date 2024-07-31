package com.hotspot.livfit.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.badge.dto.MainBadgeRequestDTO;
import com.hotspot.livfit.badge.service.UserBadgeService;
import com.hotspot.livfit.mypage.dto.MyPageResponseDTO;
import com.hotspot.livfit.mypage.dto.NicknameUpdateRequestDTO;
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

  // 마이페이지 정보 조회
  @Operation(summary = "마이페이지 정보 조회", description = "사용자의 마이페이지 정보를 조회")
  @GetMapping
  public ResponseEntity<?> getMyPageInfo(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      MyPageResponseDTO response = myPageService.getMyPageInfo(loginId);
      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
      log.error("Error during fetching my page info in controller /api/mypage: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 닉네임 업데이트
  @Operation(summary = "닉네임 변경", description = "사용자의 닉네임을 변경")
  @PutMapping("/nickname")
  public ResponseEntity<?> updateNickname(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody NicknameUpdateRequestDTO request) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      // 리퀘스트에서 새로운 닉네임 가져오기
      String newNickname = request.getNickname();

      // 닉네임 업데이트
      myPageService.updateNickname(loginId, newNickname);
      return ResponseEntity.ok("Nickname updated successfully");
    } catch (RuntimeException e) {
      log.error(
          "Error during updating nickname in controller /api/mypage/nickname: {}", e.getMessage());
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
}

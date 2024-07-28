package com.hotspot.livfit.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.mypage.dto.MyPageResponseDTO;
import com.hotspot.livfit.mypage.dto.NicknameUpdateRequestDTO;
import com.hotspot.livfit.mypage.service.MyPageService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

  private final MyPageService myPageService;
  private final JwtUtil jwtUtil;

  // 마이페이지 정보 조회
  @Operation(summary = "마이페이지 정보 조회", description = "사용자의 마이페이지 정보를 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "마이페이지 정보 조회 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "500", description = "서버 에러")
  })
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
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "500", description = "서버 에러")
  })
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
      //
      //      // 로그인 아이디와 DTO에서 받은 로그인 아이디 일치하는지 확인
      //      if (!loginId.equals(request.getLoginId())) {
      //        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
      //            .body("Login ID does not match with the token");
      //      }

      // 닉네임 업데이트
      myPageService.updateNickname(loginId, newNickname);
      return ResponseEntity.ok("Nickname updated successfully");
    } catch (RuntimeException e) {
      log.error(
          "Error during updating nickname in controller /api/mypage/nickname: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

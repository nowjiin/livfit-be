package com.hotspot.livfit.exercise.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.exercise.dto.Record;
import com.hotspot.livfit.exercise.entity.Squat;
import com.hotspot.livfit.exercise.service.ExerciseService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/squat")
@RequiredArgsConstructor
@Slf4j
public class SquatController {
  private final ExerciseService exerciseService;
  private final JwtUtil jwtUtil;

  // 스쿼트 기록 저장
  /*
  * URL: /api/squat/save_record
  * HTTP Method: POST
  * HTTP Body: record (JSON 형식)
  * 요청 JSON 형식:
  * {
  *   "login_id": "test_dev", // users의 login_id 참조
  *   "timerSec": "60",
  *   "count": "15",
  *   "perfect": "5",
  *    "great": "5",
  *    "good": "5",

  * }
  */

  @Operation(summary = "기록저장", description = "스쿼트 기록 저장")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "기록 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })
  @PostMapping("/save_record")
  public ResponseEntity<?> saveRecord(
      @RequestHeader("Authorization") String bearerToken, @RequestBody Record record) {
    log.debug("Received bearerToken: {}", bearerToken); // 토큰을 로그로 출력

    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Bearer token format");
    }

    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // JWT에서 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);

      // System.out.println(claims);
      // 토큰의 정보 중에서 로그인 아이디 추출
      String extractedLoginId = claims.getId(); // 일반적으로 로그인 아이디는 subject에 저장됩니다
      // System.out.println(extractedLoginId);
      // null 체크 추가
      if (extractedLoginId == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Invalid token: Login ID is missing");
      }

      // 추출한 로그인 아이디와 요청의 로그인 아이디가 일치하는지 확인
      if (!extractedLoginId.equals(record.getLoginId())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Login ID does not match with the token");
      }

      Squat squat =
          exerciseService.saveRecordSquat(
              extractedLoginId,
              record.getTimer_sec(),
              record.getCount(),
              record.getPerfect(),
              record.getGreat(),
              record.getGood());
      return ResponseEntity.ok(squat);
    } catch (RuntimeException e) {
      log.error(
          "Error during saving Squat record in controller /api/squat/save_record: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 스쿼트 기록 가져오기
  /*
   * URL: /api/squat/get_my_record
   * HTTP Method: GET
   */

  @Operation(summary = "기록 가져오기", description = "스쿼트 기록 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "가져오기 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })
  @GetMapping("/get_my_record")
  public ResponseEntity<?> getRecord(@RequestHeader("Authorization") String bearerToken) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // System.out.println(token);
      // 모든 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // System.out.println(claims);
      // 클레임에서 로그인 아이디 추출 -> 로그인 아이디로 사용자 뱃지 가져오기
      String jwtLoginId = claims.getId();
      // System.out.println(jwtLoginId);

      // 로그인 아이디로 사용자 뱃지 조회
      List<Squat> squats = exerciseService.getAllSquatByLoginId(jwtLoginId);
      System.out.println(squats);
      return ResponseEntity.ok(squats);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user badges in controller /api/squats/get_my_record: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

package com.hotspot.livfit.challenge.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.challenge.dto.ChallengeDTO;
import com.hotspot.livfit.challenge.dto.UserChallengeDTO;
import com.hotspot.livfit.challenge.entity.ChallengeEntity;
import com.hotspot.livfit.challenge.entity.ChallengeUserEntity;
import com.hotspot.livfit.challenge.repository.ChallengeUserRepository;
import com.hotspot.livfit.challenge.service.ChallengeService;
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
  private final ChallengeUserRepository challengeUserRepository;

  /*
   * URL: api/challenge/show/all
   * HTTP Method: GET
   * 토큰 필요 x
   */

  //  프론트 엔드가 사용하게 전체 리스트
  @Operation(summary = "전체 챌린지 조회", description = "모든 챌린지 목록 조회")
  @GetMapping("/show/all")
  public ResponseEntity<List<ChallengeEntity>> getAllChallenge() {
    List<ChallengeEntity> challengeEntity = challengeService.findAllChallenges();
    return ResponseEntity.ok(challengeEntity);
  }

  /*
   * URL: api/challenge/show/all
   * HTTP Method: GET
   * 토큰 필요 x
   */

  // 번호에 맞춰서 프론트 엔드가 사용하게
  @Operation(summary = "특정 챌린지 조회", description = "ID로 특정 챌린지 조회")
  @GetMapping("/show/{id}")
  public ResponseEntity<ChallengeEntity> getChallenge(@PathVariable Long id) {
    Optional<ChallengeEntity> challenge = challengeService.getChallenge(id);
    return challenge.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
  /*
   * URL: api/challenge/save_challenge
   * HTTP Method: POST
   * 토큰 필요 0
   *
   */

  // 챌린지 기록 저장하기 (성공 실패 여부)
  @Operation(summary = "챌린지 기록 저장", description = "사용자가 수행한 챌린지의 성공/실패 여부 저장")
  @PostMapping("/save_challenge")
  public ResponseEntity<?> saveChallenge(
      @RequestHeader("Authorization") String bearerToken, @RequestBody ChallengeDTO challengeDTO) {

    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Invalid Authorization header format.");
    }
    String token = bearerToken.substring(7).trim();
    if (token.isEmpty() || token.split("\\.").length != 3) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token format.");
    }

    try {
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String jwtLoginId = claims.getId();
      String challengeTitle = challengeDTO.getTitle();
      LocalDateTime startedAt = LocalDateTime.now();
      String success = challengeDTO.getSuccess();

      ChallengeUserEntity savedChallenge =
          challengeService.saveChallenge(jwtLoginId, challengeTitle, startedAt, success);

      return ResponseEntity.ok(savedChallenge);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while saving the challenge record: " + e.getMessage());
    }
  }
  // 마이페이지 챌린지 기록 가져오기 (성공, 실패, 진행중)
  @Operation(summary = "사용자 챌린지 기록 조회", description = "사용자의 모든 챌린지 기록 조회")
  @GetMapping("/get_challenge_record")
  public ResponseEntity<?> getAllRecords(@RequestHeader("Authorization") String bearerToken) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // 모든 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 클레임에서 로그인 아이디 추출 -> 로그인 아이디로 사용자 챌린지 가져오기
      String jwtLoginId = claims.getId();

      // 로그인 아이디로 사용자 챌린지 기록 조회
      List<UserChallengeDTO> userChallenges = challengeService.getChallengeUserById(jwtLoginId);

      if (userChallenges == null || userChallenges.isEmpty()) {
        log.info("조회된 챌린지 없음 | loginId: {}", jwtLoginId);
      } else {
        log.info("{}개의 챌린지 조회 완료. | loginId: {}", userChallenges.size(), jwtLoginId);
      }

      return ResponseEntity.ok(userChallenges);

    } catch (RuntimeException e) {
      log.error(
          "Error during fetching challenge records in controller /api/challenge/get_challenge_record: {}",
          e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}

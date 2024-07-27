package com.hotspot.livfit.challenge.controller;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.livfit.challenge.entity.Challenge;
import com.hotspot.livfit.challenge.service.ChallengeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/challenge")
@RequiredArgsConstructor
@Slf4j
public class ChallengeContorller {

  private final ChallengeService challengeService;
  /*
   * URL: api/challenge/show/{id}
   * HTTP Method: GET
   * 토큰 필요 x
   */

  @Operation(summary = "챌린지 전체 리스트", description = "챌린지 전체")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "가져오기 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })

  //  프론트 엔드가 사용하게 전체 리스트
  @GetMapping("/show/all")
  public ResponseEntity<List<Challenge>> getAllChallenge() {
    List<Challenge> challenge = challengeService.findAllChallenges();
    return ResponseEntity.ok(challenge);
  }

  /*
   * URL: api/challenge/show/all
   * HTTP Method: GET
   * 토큰 필요 x
   */

  @Operation(summary = "챌린지 id별 리스트", description = "챌린지 가져오기")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "가져오기 완료."),
    @ApiResponse(responseCode = "400", description = "잘못된 요청."),
    @ApiResponse(responseCode = "500", description = "서버 에러.")
  })

  // 번호에 맞춰서 프론트 엔드가 사용하게
  @GetMapping("/show/{id}")
  public ResponseEntity<Challenge> getChallenge(@PathVariable Long id) {
    Optional<Challenge> challenge = challengeService.getChallenge(id);
    return challenge.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}

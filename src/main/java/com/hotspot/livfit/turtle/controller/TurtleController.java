package com.hotspot.livfit.turtle.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.turtle.dto.TurtleDTO;
import com.hotspot.livfit.turtle.entity.TurtleEntity;
import com.hotspot.livfit.turtle.service.TurtleService;
import com.hotspot.livfit.user.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/turtle")
@RequiredArgsConstructor
@Slf4j
public class TurtleController {

  private final JwtUtil jwtUtil;
  private final TurtleService turtleService;
  private static final Logger logger = LoggerFactory.getLogger(TurtleController.class);

  // 거북목 기록 저장
  /*
  * URL: /api/turtle/x/save_turtle_record
  * HTTP Method: POST
  * HTTP Body: (JSON 형식)
  * 요청 JSON 형식:(닉네임 입력)
  * {
  *     "nickname": "aaa",
          "score": 1110
  * }
  */

  @Operation(summary = "비회원 거북목 기록 저장", description = "비회원일시 거북목 기록 저장")
  @PostMapping("/x/save_turtle_record")
  public ResponseEntity<?> saveTurtleRecord(@RequestBody TurtleDTO turtleData) {

    try {
      TurtleEntity entity =
          turtleService.saveRecord(
              null, turtleData.getNickname(), turtleData.getScore(), turtleData.getLocalDate());
      logger.info("거북목 비회원 기록 저장 사용자 닉네임 : {}", turtleData.getNickname());

      return ResponseEntity.ok(entity);
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while saving the record.");
    }
  }

  /*
  * URL: /api/turtle/o/save_turtle_record
  * HTTP Method: POST
  * HTTP Body: (JSON 형식)
  * 요청 JSON 형식: 토큰이 있을경우
  * {
          "score": 1110"
  * }
  */
  @Operation(summary = "회원 거북목 기록 저장", description = "회원일시 거북목 기록 저장")
  @PostMapping("/o/save_turtle_record")
  public ResponseEntity<?> savesTurtleRecord(
      @RequestHeader(value = "Authorization") String bearerToken,
      @RequestBody TurtleDTO turtleData) {

    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // JWT에서 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 토큰의 정보 중에서 로그인 아이디 추출
      String jwtLoginId = claims.getId();
      int score = turtleData.getScore();
      // 로그인된 사용자 정보에서 닉네임 추출
      String nickname = turtleData.getNickname();
      LocalDate localDate = turtleData.getLocalDate();
      logger.info("거북목 회원 기록 저장 사용자 아이디 : {}", jwtLoginId);

      try {
        TurtleEntity turtle =
            turtleService.saveRecord(jwtLoginId, nickname, score, localDate); // 사용자 닉네임과 점수를 전달

        return ResponseEntity.ok(turtle);
      } catch (RuntimeException ex) {
        log.error("Record Not Found Error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
      } catch (Exception ex) {
        log.error("Internal Server Error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An error occurred while saving the record.");
      }
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }
  }

  /*
   * URL: /api/turtle/all_record
   * HTTP Method: GET
   */

  @GetMapping("/all-records")
  @Operation(summary = "거북목 전체 리스트 조회(순위 조회) ", description = "거북목 순위")
  public ResponseEntity<List<TurtleDTO>> getAllRecords() {
    List<TurtleDTO> records = turtleService.findAllRecords();
    return ResponseEntity.ok(records);
  }

  /*
   * URL: /api/turtle/my-records
   * HTTP Method: GET
   */
  @Operation(summary = "거북목 회원 기록 조회 ", description = "거북목 회원 기록")
  @GetMapping("/my-records")
  public ResponseEntity<?> getRecordById(
      @RequestHeader(value = "Authorization") String bearerToken) {
    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // JWT에서 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 토큰의 정보 중에서 로그인 아이디 추출
      String jwtLoginId = claims.getId();

      logger.info("거북목 기록 조회, 사용자 아이디: {}", jwtLoginId);
      // 조회(로그인 아이디로)
      List<TurtleDTO> records = turtleService.getTurtleRecordsByLoginId(jwtLoginId);
      return ResponseEntity.ok(records);
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user squat in controller /api/squats/get_my_record: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}

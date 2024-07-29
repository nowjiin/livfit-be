package com.hotspot.livfit.turtle.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotspot.livfit.turtle.dto.TurtleDTO;
import com.hotspot.livfit.turtle.entity.TurtleEntity;
import com.hotspot.livfit.turtle.repository.TurtleRepository;
import com.hotspot.livfit.turtle.service.TurtleService;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;
import com.hotspot.livfit.user.util.JwtUtil;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/turtle")
@RequiredArgsConstructor
@Slf4j
public class TurtleController {

  @Autowired private JwtUtil jwtUtil;
  @Autowired private UserRepository userRepository;
  @Autowired private TurtleRepository turtleRepository;
  @Autowired private TurtleService turtleService;

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

  @PostMapping("/x/save_turtle_record")
  public ResponseEntity<?> saveTurtleRecord(
      @RequestHeader(value = "Authorization", required = false) String bearerToken,
      @RequestBody TurtleEntity turtleData) {

    String jwtLoginId = null;

    if (jwtLoginId == null
        && (turtleData.getNickname() == null || turtleData.getNickname().trim().isEmpty())) {
      return ResponseEntity.badRequest().body("A nickname is required for non-logged-in users.");
    }

    try {
      TurtleEntity savedTurtle =
          turtleService.saveRecord(jwtLoginId, turtleData.getNickname(), turtleData.getScore());
      return ResponseEntity.ok(savedTurtle);
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
  @PostMapping("/o/save_turtle_record")
  public ResponseEntity<?> savesTurtleRecord(
      @RequestHeader(value = "Authorization") String bearerToken,
      @RequestBody TurtleEntity turtleData) {
    int score = turtleData.getScore();
    String nickname = turtleData.getNickname();

    try {
      // Bearer 토큰에서 JWT 추출
      String token = bearerToken.substring(7);
      // JWT에서 클레임 추출
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      // 토큰의 정보 중에서 로그인 아이디 추출
      String jwtLoginId = claims.getId();
      score = turtleData.getScore();
      User user =
          userRepository
              .findByLoginId(jwtLoginId)
              .orElseThrow(() -> new RuntimeException("User not found with login ID: "));
      nickname = user.getNickname(); // 로그인된 사용자의 닉네임을 가져옴

      try {
        TurtleEntity savedTurtle =
            turtleService.saveRecord(jwtLoginId, nickname, score); // 사용자 닉네임과 점수를 전달
        return ResponseEntity.ok(savedTurtle);
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
  public ResponseEntity<List<TurtleDTO>> getAllRecords() {
    List<TurtleDTO> records = turtleService.findAllRecords();
    if (records.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(records);
  }
  /*
   * URL: /api/turtle/top_records
   * HTTP Method: GET
   */
  @GetMapping("/top3-records-direct")
  public ResponseEntity<List<TurtleDTO>> getTopThreeRecordsDirect() {
    List<TurtleDTO> topRecords = turtleService.findTopThreeRecordsDirect();
    if (topRecords.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(topRecords);
  }
}

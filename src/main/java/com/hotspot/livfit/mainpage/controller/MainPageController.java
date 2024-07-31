package com.hotspot.livfit.mainpage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.livfit.mainpage.dto.MainPageDTO;
import com.hotspot.livfit.mainpage.service.MainPageService;
import com.hotspot.livfit.user.util.JwtUtil;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MainPageController {
  private final JwtUtil jwtUtil;
  private final MainPageService mainPageService;
  private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);

  @GetMapping("/mainpage")
  public ResponseEntity<?> getMain(@RequestHeader("Authorization") String bearerToken) {
    try {
      if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Invalid Authorization header format.");
      }
      String token = bearerToken.substring(7).trim();
      if (token.isEmpty() || token.split("\\.").length != 3) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token format.");
      }
      token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId();

      logger.info("메인페이지에 있는, 사용자 아이디: {}", loginId);

      MainPageDTO responses = mainPageService.getMainPageInfo(loginId);
      return ResponseEntity.ok(responses);
    } catch (Exception e) {
      log.error(
          "Error during fetching mainpage info in controller /api/mainpage: {}", e.getMessage());
      return ResponseEntity.badRequest().body("Error retrieving main page info: " + e.getMessage());
    }
  }
}

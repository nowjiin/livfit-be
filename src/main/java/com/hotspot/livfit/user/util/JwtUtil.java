package com.hotspot.livfit.user.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Component
public class JwtUtil {
  @Value("${secret-key}")
  private String secretKey;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  // Access Token 발급 부분
  public String createAccessToken(String userId, String userName) {
    Date expireTime = Date.from(Instant.now().plus(23, ChronoUnit.HOURS));
    Key key = getSigningKey();
    return Jwts.builder()
        .setId(userId)
        .setSubject(userName)
        .setIssuedAt(new Date())
        .setExpiration(expireTime)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }
  // Refresh Token 발급 부분
  public String createRefreshToken(String userId) {
    Date expireTime = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));
    Key key = getSigningKey();
    return Jwts.builder()
        .setId(userId)
        .setIssuedAt(new Date())
        .setExpiration(expireTime)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token, String username) {
    try {
      Claims claims =
          Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
      String extractedUsername = claims.getId();

      if (!extractedUsername.equals(username)) {
        log.info("JWT 토큰의 사용자 이름이 일치하지 않습니다.");
        return false;
      }
      if (claims.getExpiration().before(new Date())) {
        log.info("만료된 JWT 토큰입니다.");
        return false;
      }
      // 너무 자주나오고 길어서 주석처리함
      // log.info("JWT validation 성공");
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
      return false;
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
      return false;
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
      return false;
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
      return false;
    } catch (Exception e) {
      log.info("기타 오류 발생");
      return false;
    }
  }

  // Claims에서 모든 정보 추출
  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // 토큰에서 사용자 이름 추출
  public String extractUsername(String token) {
    return getAllClaimsFromToken(token).getId();
  }

  // 토큰에서 만료 시간 추출
  public Date extractExpiration(String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  // 토큰이 만료되었는지 확인
  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }
}

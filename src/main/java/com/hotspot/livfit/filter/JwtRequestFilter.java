package com.hotspot.livfit.filter;

import java.io.IOException;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;
import com.hotspot.livfit.user.util.JwtUtil;

/*
* 코드 흐름
* 1. 토큰 추출:
* => 요청 헤더에서 JWT 토큰을 추출.

* 2. 토큰 유효성 검사:
* => 추출한 JWT 토큰의 유효성을 검사 (validate).

* 3. 사용자 인증 상태 확인:
* => 추출한 토큰에서 사용자명을 추출하고,
* SecurityContextHolder에 인증 정보가 설정되어 있는지 확인.

* 4. 사용자 정보 조회 및 인증 설정:
* => 사용자가 인증되지 않은 상태(SecurityContextHolder.getContext().getAuthentication() == null)
* 데이터베이스에서 사용자 정보를 조회.
* JWT 토큰이 유효하고, 데이터베이스에서 사용자를 찾을 수 있으면, UserDetails 객체를 생성.
* UsernamePasswordAuthenticationToken을 생성 후, SecurityContextHolder에 설정.
* */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  // Repo 를 거쳐 User 를 꺼내오기
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    try {
      // Authorization 헤더에서 JWT 토큰을 가져옴
      final String authorizationHeader = request.getHeader("Authorization");
      String username = null;
      String jwt = null;

      // Bearer 토큰인지 확인하고, JWT 토큰에서 Username 추출
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        username = jwtUtil.extractUsername(jwt);
      }

      /*--------------------------------
        사용자가 인증되지 않은 상태
        1. 로그인 하지 않은 상태  SecurityContextHolder에 사용자의 인증 정보가 설정되지 않은 경우.
        2. 잘못된 또는 만료된 토큰
        3. 토큰이 없는 경우:
      ---------------------------------*/
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        // 사용자 정보 조회
        User user = userRepository.findByLoginId(username).orElse(null);
        // 사용자가 존재하고, JWT 토큰이 유효하다면
        if (user != null && jwtUtil.validateToken(jwt, user.getLoginId())) {
          UserDetails userDetails =
              new org.springframework.security.core.userdetails.User(
                  user.getLoginId(),
                  user.getLoginPw(),
                  Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole())));
          // UsernamePasswordAuthenticationToken 생성
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          usernamePasswordAuthenticationToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
      }
    } catch (Exception e) {
      log.info("JWT Authentication Filter Failed");
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
      return;
    }
    chain.doFilter(request, response);
  }
}

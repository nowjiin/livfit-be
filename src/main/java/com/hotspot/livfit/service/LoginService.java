package com.hotspot.livfit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import com.hotspot.livfit.dto.LoginDto;
import com.hotspot.livfit.jwt.JWTUtil;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public String login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            if (authentication.isAuthenticated()) {
                // 역할 가져오기
                String role = authentication.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .findFirst()
                        .orElse("");

                // JWT 토큰 발급
                return jwtUtil.createJwt(loginDto.getUsername(), role, 60 * 60 * 1000L); // 1시간 유효 토큰
            }
        } catch (AuthenticationException e) {
            // 인증 실패
            e.printStackTrace(); // 인증 실패 로그 출력
            return null;
        }
        return null;
    }
}

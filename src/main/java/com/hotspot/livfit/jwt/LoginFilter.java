package com.hotspot.livfit.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.hotspot.livfit.dto.CustomUserDetails;

import java.util.Collection;
import java.util.Iterator;

// 기존 폼 로그인 => UsernamePasswordAuthenticationFilter가 활성화 되어 있어서 따로 설정할 필요 x
// 해당 프로젝트에서는 formLogin disable 설정했으므로 강제로 만들어서 등록
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // username과 password 꺼내기 => obtainUsername / obtainPassword(HttpServletRequest 객체)
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("username = " + username);
        System.out.println("password = " + password);

        // 인증 진행 : UsernamePasswordAuthenticationFilter가 AuthenticationManager에게
        //    username, password 를 전달 (DTO에 담아서) => DTO : UsernamePasswordAuthenticationToken!!!
        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(username, password, null);
        // 인자 설명 => 1: username, 2: password, 3: role

        // authenticationManager에게 authToken 전달
        // => 자동으로 검증 진행 (DB에서 회원정보 가져와서 검증 방식)
        // => 검증 성공하면 successfulAuthentication 메서드 실행됨!
        // => 검증 실패하면 unsuccessfulAuthentication 메서드 실행됨!
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공 시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication){

        // username 추출
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        // role 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWTUtil에 token 생성 요청
        String token = jwtUtil.createJwt(username, role, 60*60*1000L);

        // JWT를 response에 담아서 응답 (header 부분에)
        // key : "Authorization"
        // value : "Bearer " (인증방식) + token
        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed){

        // 실패 시 401 응답코드 보냄
        response.setStatus(401);
    }
}

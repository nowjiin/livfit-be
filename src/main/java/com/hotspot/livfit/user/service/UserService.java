package com.hotspot.livfit.user.service;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;
import com.hotspot.livfit.user.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  // 회원가입 비지니스 로직
  public User registerUser(String loginId, String password, String nickname) {
    // 중복된 loginId가 있는지 확인
    if (userRepository.findByLoginId(loginId).isPresent()) {
      throw new RuntimeException("Login ID already exists");
    }
    // 중복된 nickname이 있는지 확인
    if (userRepository.findByNickname(nickname).isPresent()) {
      throw new RuntimeException("Nickname already exists");
    }

    // 새로운 사용자 생성
    User user = new User();
    user.setLoginId(loginId);
    // 비밀번호 암호화
    user.setLoginPw(passwordEncoder.encode(password));
    user.setNickname(nickname);
    user.setUserRole("ROLE_USER");
    // User 정보 DB에 저장
    return userRepository.save(user);
  }

  // 로그인 로직
  public String loginUser(String loginId, String password) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("Invalid login ID or password"));

    if (!passwordEncoder.matches(password, user.getLoginPw())) {
      throw new RuntimeException("Invalid login ID or password");
    }

    return jwtUtil.createAccessToken(user.getLoginId(), user.getNickname());
  }

  // Refresh 토큰 저장 로직
  public User saveRefreshToken(String loginId, String refreshToken) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    user.setRefreshToken(refreshToken);
    return userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByLoginId(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole()));

    return new org.springframework.security.core.userdetails.User(
        user.getLoginId(), user.getLoginPw(), authorities);
  }
}

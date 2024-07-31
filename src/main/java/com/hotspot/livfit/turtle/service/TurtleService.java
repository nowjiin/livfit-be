package com.hotspot.livfit.turtle.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.turtle.dto.TurtleDTO;
import com.hotspot.livfit.turtle.entity.TurtleEntity;
import com.hotspot.livfit.turtle.repository.TurtleRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;
import com.hotspot.livfit.user.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class TurtleService {
  private final UserRepository userRepository;
  private final TurtleRepository turtleRepository;
  private final JwtUtil jwtUtil;

  // 거북목 기록 저장(회원& 비호원)
  public TurtleEntity saveRecord(
      String jwtLoginId, String nickname, int score, LocalDate localDate) {
    User user = null;
    if (jwtLoginId != null && !jwtLoginId.trim().isEmpty()) {
      user =
          userRepository
              .findByLoginId(jwtLoginId)
              .orElseThrow(
                  () -> new RuntimeException("User not found with login ID: " + jwtLoginId));
    }

    TurtleEntity turtle = new TurtleEntity();

    if (user != null) {
      turtle.setUser(user);
      turtle.setNickname(user.getNickname()); // 사용자가 로그인한 경우 User 엔티티의 닉네임 사용
    } else {
      turtle.setNickname(nickname); // 로그인하지 않은 경우 입력받은 닉네임 사용
    }
    turtle.setScore(score);
    turtle.setLocalDate(localDate);
    return turtleRepository.save(turtle);
  }

  // 거북목 저장된 기록(순위 조회)
  public List<TurtleDTO> findAllRecords() {
    return turtleRepository.findAllRecords();
  }

  // 거북목 사용자 아이디로 조회하기
  @Transactional(readOnly = true)
  public List<TurtleDTO> getTurtleRecordsByLoginId(String loginId) {
    List<TurtleEntity> turtles = turtleRepository.findByLoginId(loginId);
    return turtles.stream().map(this::convertToTurtleDTO).collect(Collectors.toList());
  }

  // dto로 변환하기
  private TurtleDTO convertToTurtleDTO(TurtleEntity turtle) {
    TurtleDTO dto = new TurtleDTO();
    dto.setLoginId(turtle.getUser().getLoginId());
    dto.setNickname(turtle.getNickname());
    dto.setScore(turtle.getScore());
    dto.setLocalDate(turtle.getLocalDate());
    return dto;
  }
}

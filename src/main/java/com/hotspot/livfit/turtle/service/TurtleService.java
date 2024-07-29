package com.hotspot.livfit.turtle.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.turtle.dto.TurtleDTO;
import com.hotspot.livfit.turtle.entity.TurtleEntity;
import com.hotspot.livfit.turtle.repository.TurtleRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class TurtleService {
  private final UserRepository userRepository;
  private final TurtleRepository turtleRepository;

  public TurtleEntity saveRecord(String jwtLoginId, String nickname, int score) {
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
      turtle.setNickname(user.getNickname()); // 사용자가 로그인한 경우 User 엔티티의 닉네임 사용
    } else {
      turtle.setNickname(nickname); // 로그인하지 않은 경우 입력받은 닉네임 사용
    }
    turtle.setScore(score);
    return turtleRepository.save(turtle);
  }

  public List<TurtleDTO> findAllRecords() {
    return turtleRepository.findAllRecords();
  }


}

package com.hotspot.livfit.point.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.point.dto.PointRequestDTO;
import com.hotspot.livfit.point.entity.PointHistory;
import com.hotspot.livfit.point.repository.PointHistoryRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PointService {
  private final PointHistoryRepository pointHistoryRepository;
  private final UserRepository userRepository;

  @Transactional
  public void updatePoints(String loginId, PointRequestDTO pointRequestDTO) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 특정 사용자 ID의 누적 포인트 히스토리 조회
    List<PointHistory> history = pointHistoryRepository.findByUser_LoginId(loginId);

    // 누적포인트 계산
    //    int totalPoints = history.isEmpty() ? 0 : history.get(history.size() -
    // 1).getTotalPoints();
    int totalPoints = history.stream().mapToInt(PointHistory::getTotalPoints).sum();

    // 포인트 적립 or 차감 계산
    if (pointRequestDTO.getType().equals("earn")) { // 적립
      totalPoints += pointRequestDTO.getPoints();
    } else if (pointRequestDTO.getType().equals("spend")) { // 차감
      totalPoints -= pointRequestDTO.getPoints();
    }

    PointHistory pointHistory = new PointHistory();
    pointHistory.setUser(user);
    pointHistory.setEventTime(LocalDateTime.now()); // 이벤트 발생 시간
    pointHistory.setPoints(pointRequestDTO.getPoints()); // 적립하거나 차감된 포인트 값
    pointHistory.setTotalPoints(totalPoints); // 누적 포인트 값
    pointHistory.setType(pointRequestDTO.getType()); // 포인트 적립/차감 타입 설정
    pointHistory.setDescription(pointRequestDTO.getDescription()); // 포인트 적립/차감에 대한 설명
    pointHistoryRepository.save(pointHistory);
  }

  // 특정 사용자의 포인트 히스토리 조회
  public List<PointHistory> getPointHistory(String loginId) {
    return pointHistoryRepository.findByUser_LoginId(loginId);
  }

  // 특정 사용자의 누적 포인트 조회
  public int getTotalPoints(String loginId) {
    // 특정 사용자 ID의 누적 포인트 히스토리 조회
    List<PointHistory> history = pointHistoryRepository.findByUser_LoginId(loginId);

    // 누적포인트 계산
    return history.isEmpty() ? 0 : history.get(history.size() - 1).getTotalPoints();
  }
}

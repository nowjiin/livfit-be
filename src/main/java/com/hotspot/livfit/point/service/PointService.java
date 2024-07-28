package com.hotspot.livfit.point.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotspot.livfit.point.dto.PointHistoryDTO;
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

    // 누적포인트 계산 -> 마지막 기록의 총 누적 포인트 가져오기
    // 이전 코드를 보니 모든 기록의 누적 포인트를 합산해서 중복 계산 문제가 발생했던 것
    // 가장 최신 기록의 누적 포인트를 가져와서 계산하는 것으로 수정
    int totalPoints = history.isEmpty() ? 0 : history.get(history.size() - 1).getTotalPoints();

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
  public List<PointHistoryDTO> getPointHistory(String loginId) {
    // 사용자 로그인 ID를 사용하여 해당 사용자의 포인트 히스토리 조회
    List<PointHistory> history = pointHistoryRepository.findByUser_LoginId(loginId);
    // PointHistoryDTO를 담을 리스트 생성하고
    List<PointHistoryDTO> historyDTOs = new ArrayList<>();
    // 각 포인트 히스토리 객체?를 PointHistoryDTO로 변환해서 리스트에 추가하는 로직입니다~
    for (PointHistory h : history) {
      PointHistoryDTO dto =
          new PointHistoryDTO(
              h.getUser().getId(), // ID
              h.getUser().getLoginId(), // 로그인 ID
              h.getUser().getNickname(), // 닉네임
              h.getTotalPoints(), // 누적 포인트
              h.getType(), // 포인트 타입 (적립 or 차감)
              h.getDescription() // 포인트 설명
              );
      historyDTOs.add(dto);
    }
    return historyDTOs;
  }
}

package com.hotspot.livfit.point.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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
    int totalPoints = history.isEmpty() ? 0 : history.get(history.size() - 1).getTotalPoints();

    // 포인트 적립 or 차감 계산
    if (pointRequestDTO.getType().equals("earn")) { // 적립
      totalPoints += pointRequestDTO.getPoints();
      log.info("User: {} | {}p Earn", loginId, pointRequestDTO.getPoints());
    } else if (pointRequestDTO.getType().equals("spend")) { // 차감
      totalPoints -= pointRequestDTO.getPoints();
      log.info("User: {} | {}p Spend", loginId, pointRequestDTO.getPoints());
    }

    PointHistory pointHistory = new PointHistory();
    pointHistory.setUser(user);
    pointHistory.setEventTime(LocalDate.now()); // 이벤트 발생 시간
    pointHistory.setPoints(pointRequestDTO.getPoints()); // 적립하거나 차감된 포인트 값
    pointHistory.setTotalPoints(totalPoints); // 누적 포인트 값
    pointHistory.setType(pointRequestDTO.getType()); // 포인트 적립/차감 타입 설정
    pointHistory.setTitle(pointRequestDTO.getTitle());
    pointHistory.setDescription(pointRequestDTO.getDescription()); // 포인트 적립/차감에 대한 설명
    pointHistoryRepository.save(pointHistory);

    log.info("{}'s Total Points: {}p", loginId, totalPoints);
  }

  // 특정 사용자의 포인트 히스토리 조회
  public List<PointHistoryDTO> getPointHistory(String loginId) {
    log.info("Point history for user with login ID: {}", loginId);

    // 사용자 로그인 ID를 사용하여 해당 사용자의 포인트 히스토리 조회
    List<PointHistory> history = pointHistoryRepository.findByUser_LoginId(loginId);

    if (history.isEmpty()) {
      log.warn("No point history found for user with login ID: {}", loginId);
    } else {
      log.info("Point history successfully retrieved for user with login ID: {}", loginId);
    }

    // PointHistoryDTO를 담을 리스트 생성하고
    List<PointHistoryDTO> historyDTOs = new ArrayList<>();
    for (PointHistory h : history) {
      PointHistoryDTO dto =
          new PointHistoryDTO(
              h.getUser().getId(), // ID
              h.getUser().getLoginId(), // 로그인 ID
              h.getUser().getNickname(), // 닉네임
              h.getPoints(), // 적립 or 차감 포인트값
              h.getTotalPoints(), // 누적 포인트
              h.getType(), // 포인트 타입 (적립 or 차감)
              h.getTitle(),
              h.getDescription(), // 포인트 설명
              h.getEventTime());
      historyDTOs.add(dto);
    }
    return historyDTOs;
  }

  // 회원가입 환영 포인트 지급
  @Transactional
  public void assignWelcomeBonus(String loginId) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + loginId));

    PointHistory pointHistory = new PointHistory();
    pointHistory.setUser(user);
    pointHistory.setPoints(1000);
    pointHistory.setTotalPoints(1000);
    pointHistory.setType("EARN");
    pointHistory.setTitle("회원가입 1000p");
    pointHistory.setDescription("LIVFIT에 오신 것을 환영합니다!");
    pointHistory.setEventTime(LocalDate.now());

    pointHistoryRepository.save(pointHistory);
  }

  @Transactional
  public void rewardPointsForChallengeCompletion(String loginId, int points) {
    User user =
        userRepository
            .findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 사용자의 모든 포인트 기록을 조회하여 누적 포인트 계산
    List<PointHistory> pointHistories = pointHistoryRepository.findByUser_LoginId(loginId);
    int currentTotalPoints = pointHistories.stream().mapToInt(PointHistory::getPoints).sum();

    // 새로운 포인트 기록 추가
    PointHistory pointHistory = new PointHistory();
    pointHistory.setUser(user);
    pointHistory.setPoints(points);
    pointHistory.setTotalPoints(currentTotalPoints + points);
    pointHistory.setType("EARN");
    pointHistory.setTitle("챌린지 완료");
    pointHistory.setDescription("챌린지 완료 축하 300p 지급");
    pointHistory.setEventTime(LocalDate.now());

    // 포인트 기록 저장
    pointHistoryRepository.save(pointHistory);
  }
}

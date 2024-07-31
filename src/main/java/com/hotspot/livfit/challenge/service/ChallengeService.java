package com.hotspot.livfit.challenge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.challenge.dto.UserChallengeDTO;
import com.hotspot.livfit.challenge.entity.ChallengeEntity;
import com.hotspot.livfit.challenge.entity.ChallengeUserEntity;
import com.hotspot.livfit.challenge.repository.ChallengeRepository;
import com.hotspot.livfit.challenge.repository.ChallengeUserRepository;
import com.hotspot.livfit.user.entity.User;
import com.hotspot.livfit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ChallengeService {
  private final ChallengeRepository challengeRepository;
  private final ChallengeUserRepository challengeUserRepository;
  private final UserRepository userRepository;

  public Optional<ChallengeEntity> getChallenge(Long id) {
    return challengeRepository.findByNumberId(id);
  }

  public List<ChallengeEntity> findAllChallenges() {
    return challengeRepository.findAll();
  }

  public ChallengeUserEntity saveChallenge(
      String jwtLoginId, String title, LocalDateTime startedAt, String success) {
    User user =
        userRepository
            .findByLoginId(jwtLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    ChallengeEntity challengeEntity =
        challengeRepository
            .findByTitle(title)
            .orElseThrow(() -> new RuntimeException("Challenge not found with ID: " + title));

    // ChallengeUser 엔티티에서 로그인 아이디 참조 변경에 따라 수정함
    ChallengeUserEntity challengeUserEntity = new ChallengeUserEntity();
    challengeUserEntity.setUser(user);
    challengeUserEntity.setChallenge(challengeEntity);
    challengeUserEntity.setSuccess(success);
    challengeUserEntity.setStartedAt(startedAt);
    challengeUserEntity.setTitle(challengeEntity.getTitle());

    return challengeUserRepository.save(challengeUserEntity);
  }

  // 사용자 로그인 ID로 챌린지 기록 조회
  public List<UserChallengeDTO> getChallengeUserById(String loginId) {
    return challengeUserRepository.findByLoginId(loginId).stream()
        .map(this::convertToUserChallengeDTO)
        .collect(Collectors.toList());
  }

  private UserChallengeDTO convertToUserChallengeDTO(ChallengeUserEntity entity) {
    User user = entity.getUser();
    return new UserChallengeDTO(
        entity.getId(),
        user.getLoginId(),
        user.getNickname(),
        entity.getChallenge().getTitle(),
        entity.getChallenge().getContent(),
        entity.getStartedAt(),
        entity.getSuccess());
  }
}

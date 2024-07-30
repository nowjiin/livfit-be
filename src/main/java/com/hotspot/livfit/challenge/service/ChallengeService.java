package com.hotspot.livfit.challenge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.challenge.dto.ChallengeDTO;
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

    ChallengeUserEntity challengeUserEntity = new ChallengeUserEntity();
    challengeUserEntity.setLoginId(user.getLoginId());
    challengeUserEntity.setTitle(challengeEntity.getTitle());
    challengeUserEntity.setSuccess(success);
    challengeUserEntity.setStartedAt(startedAt);

    return challengeUserRepository.save(challengeUserEntity);
  }

  public List<ChallengeDTO> getChallengeUserById(String jwtLoginId) {
    List<ChallengeUserEntity> challengeUserEntities =
        challengeUserRepository.findByLoginId(jwtLoginId);

    return challengeUserEntities.stream()
        .map(this::convertToChallengeDTO)
        .collect(Collectors.toList());
  }

  private ChallengeDTO convertToChallengeDTO(ChallengeUserEntity entity) {
    ChallengeDTO dto = new ChallengeDTO();
    dto.setLoginId(entity.getUser().getLoginId());
    dto.setStartedAt(entity.getStartedAt());
    dto.setSuccess(entity.getSuccess());
    if (entity.getChallenge() != null) {
      dto.setTitle(entity.getChallenge().getTitle());
    } else {
      dto.setTitle("No title available");
    }
    return dto;
  }
}

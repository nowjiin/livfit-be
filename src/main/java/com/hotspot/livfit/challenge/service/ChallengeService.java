package com.hotspot.livfit.challenge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

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
      String jwtLoginId, Long challengeId, LocalDateTime startedAt, String success) {
    User user =
        userRepository
            .findByLoginId(jwtLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    ChallengeEntity challengeEntity =
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new RuntimeException("Challenge not found with ID: " + challengeId));

    ChallengeUserEntity challengeUserEntity = new ChallengeUserEntity();
    challengeUserEntity.setUser(user);
    challengeUserEntity.setChallenge(challengeEntity);
    challengeUserEntity.setStartedAt(startedAt);
    challengeUserEntity.setSuccess(success);

    return challengeUserRepository.save(challengeUserEntity);
  }

  public List<ChallengeUserEntity> getChallengeUserById(String jwtLoginId) {
    User user =
        userRepository
            .findByLoginId(jwtLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with login ID: " + jwtLoginId));

    return challengeUserRepository.findByLoginId(jwtLoginId);
  }
}

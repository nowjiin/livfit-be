package com.hotspot.livfit.challenge.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotspot.livfit.challenge.entity.Challenge;
import com.hotspot.livfit.challenge.repository.ChallengeRepository;

@Service
@RequiredArgsConstructor
public class ChallengeService {
  private final ChallengeRepository challengeRepository;

  public Optional<Challenge> getChallenge(Long id) {
    return challengeRepository.findById(id);
  }

  public List<Challenge> findAllChallenges() {
    return challengeRepository.findAll();
  }
}

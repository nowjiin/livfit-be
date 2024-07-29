package com.hotspot.livfit.challenge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotspot.livfit.challenge.entity.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
  @Query("SELECT a from Challenge a WHERE a.id = :id")
  // db가 없을 경우를 대비해 optional로 작성함 (optional.empty()가 반환 됨)
  Optional<Challenge> findById(Long id);

  @Query("SELECT c FROM Challenge c")
  List<Challenge> findAllChallenges();
}

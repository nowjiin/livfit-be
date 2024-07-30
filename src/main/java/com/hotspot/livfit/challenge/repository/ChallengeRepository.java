package com.hotspot.livfit.challenge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.challenge.entity.ChallengeEntity;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, Long> {
  @Query("SELECT a from ChallengeEntity a WHERE a.id = :id")
  Optional<ChallengeEntity> findByNumberId(Long id);

  @Query("SELECT c FROM ChallengeEntity c")
  List<ChallengeEntity> findAllChallenges();

  @Query("SELECT c FROM ChallengeEntity c WHERE c.title = :title")
  Optional<ChallengeEntity> findByTitle(@Param("title") String title);
}

package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotspot.livfit.exercise.entity.Lunge;

public interface LungeRepository extends JpaRepository<Lunge, Long> {
  @Query("SELECT s FROM Lunge s WHERE s.user.loginId = :loginId")
  List<Lunge> findByLoginId(String loginId);
}

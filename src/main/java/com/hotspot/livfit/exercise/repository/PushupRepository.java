package com.hotspot.livfit.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotspot.livfit.exercise.entity.Pushup;

public interface PushupRepository extends JpaRepository<Pushup, Long> {
  @Query("SELECT s FROM Pushup s WHERE s.user.loginId = :loginId")
  List<Pushup> findByLoginId(String loginId);
}

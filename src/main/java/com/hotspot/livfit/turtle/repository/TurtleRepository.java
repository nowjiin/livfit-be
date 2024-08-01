package com.hotspot.livfit.turtle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.turtle.dto.TurtleDTO;
import com.hotspot.livfit.turtle.entity.TurtleEntity;

public interface TurtleRepository extends JpaRepository<TurtleEntity, Long> {
  @Query("SELECT t FROM TurtleEntity t WHERE t.user.loginId = :loginId")
  List<TurtleEntity> findByLoginId(@Param("loginId") String loginId);

  @Query(
      "SELECT new com.hotspot.livfit.turtle.dto.TurtleDTO(t.nickname, t.score, t.localDate) FROM TurtleEntity t")
  List<TurtleDTO> findAllRecords();
}

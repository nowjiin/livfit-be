package com.hotspot.livfit.turtle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotspot.livfit.turtle.dto.TurtleDTO;
import com.hotspot.livfit.turtle.entity.TurtleEntity;

public interface TurtleRepository extends JpaRepository<TurtleEntity, Long> {
  @Query("SELECT s FROM TurtleEntity s WHERE s.user.loginId = :loginId")
  List<TurtleEntity> findByLoginId(String loginId);

  @Query(
      "SELECT new com.hotspot.livfit.turtle.dto.TurtleDTO(p.nickname, p.score) FROM TurtleEntity p ORDER BY p.score DESC")
  List<TurtleDTO> findAllRecords();
}

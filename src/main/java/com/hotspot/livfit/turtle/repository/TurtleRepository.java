package com.hotspot.livfit.turtle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotspot.livfit.turtle.dto.TurtleDTO;
import com.hotspot.livfit.turtle.entity.TurtleEntity;

public interface TurtleRepository extends JpaRepository<TurtleEntity, Long> {
  @Query("SELECT t FROM TurtleEntity t WHERE t.user.nickname = :nickname")
  List<TurtleEntity> findByUserNickname(@Param("nickname") String nickname);

  @Query(
      "SELECT new com.hotspot.livfit.turtle.dto.TurtleDTO(p.nickname, p.score, p.localDate ) FROM TurtleEntity p ORDER BY p.score DESC")
  List<TurtleDTO> findAllRecords();
}

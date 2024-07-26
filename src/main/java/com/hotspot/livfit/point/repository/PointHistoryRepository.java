package com.hotspot.livfit.point.repository; // package com.hotspot.livfit.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotspot.livfit.point.entity.PointHistory;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
  // 로그인 아이디로 포인트 기록을 조회
  List<PointHistory> findByUser_LoginId(String loginId);
}

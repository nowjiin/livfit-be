package com.hotspot.livfit.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotspot.livfit.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLoginId(String loginId);

  Optional<User> findByNickname(String nickname);
}

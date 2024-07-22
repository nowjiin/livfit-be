package com.hotspot.livfit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotspot.livfit.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);
}

package com.hotspot.livfit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.hotspot.livfit.dto.JoinDto;
import com.hotspot.livfit.entity.UserEntity;
import com.hotspot.livfit.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto) {

        String username = joinDto.getUsername();
        String password = joinDto.getPassword();
        String role = joinDto.getRole();

        if (userRepository.existsByUsername(username)) {
            // username이 이미 존재하면 강제 리턴 (회원가입x)
            return;
        }
        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(username);
        // 비밀번호 암호화
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        // 역할 설정 (기본 역할을 설정하거나 입력받은 역할을 설정)
        userEntity.setRole(role != null ? role : "ROLE_USER");

        userRepository.save(userEntity);
    }
}

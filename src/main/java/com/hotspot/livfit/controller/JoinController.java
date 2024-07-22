package com.hotspot.livfit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.hotspot.livfit.dto.JoinDto;
import com.hotspot.livfit.service.JoinService;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDto joinDto) {
        joinService.joinProcess(joinDto);
        return ResponseEntity.ok("ok");
    }

}

package com.hotspot.livfit.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Iterator;

@Controller
@ResponseBody   // 웹이 아닌 특정한 문자열 데이터를 응답하도록 만듦
public class MainController {

    @GetMapping("/")
    public String main() {

        // JWT Filter를 통과한 뒤 세션의 정보 확인 => 세션의 현재 username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 세션의 현재 사용자 role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "Main Controller \nusername : " + username + "\nrole : " + role;
    }
}

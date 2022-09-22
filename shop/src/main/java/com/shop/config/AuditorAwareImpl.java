package com.shop.config;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

// AuditorAware : Entity 가 저장 또는 수정될 때 자동으로 값을 업데이트 해주는 기능
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

                                // SecurityContextHolder : 누가 인증했는지에 대한 정보들을 저장
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if (authentication != null){
            userId = authentication.getName();  // 현재 로그인 한 사용자의 정보를 조회해 사용자의 이름을 등록자와 수정자로 지정
        }
        return Optional.of(userId);
    }
}

package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 스프링 컨테이너는 @Configuration이 붙어있는 클래스를 자동으로 빈으로 등록해두고, 해당 클래스를 파싱해서 @Bean이 있는 메소드를 찾아서 빈을 생성함
@Configuration
@EnableJpaAuditing  // JPA의 Auditing 기능 활성화
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() { // 등록자와 수정자를 처리해주는 AuditorAware 을 빈으로 등록
        return new AuditorAwareImpl();
    }
}

package shop.mtcoding.hiberpc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.hiberpc.config.filter.MyBlackListFilter;

import javax.servlet.Filter;

@Configuration
public class FliterRegisterConfig {

    @Bean
    public FilterRegistrationBean<?> blackListFilter() {
        FilterRegistrationBean<MyBlackListFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MyBlackListFilter());
        registration.addUrlPatterns("/filter");
        registration.setOrder(1); // 필터의 순서. 1이 제일 먼저 실행.

        return registration;
    }
}

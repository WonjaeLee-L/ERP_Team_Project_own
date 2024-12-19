package com.example.practice.SecurityConfig;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http            .csrf((csrfConfig) -> csrfConfig.disable())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/css/**",
                                "/image/**",
                                "/static/**",
                                "/loginCheckId",
                                "/loginSignup",
                                "/external/**",
                                "/external/productImg/**",
                                "/productImg/**"
                        ).permitAll()
                        .requestMatchers(
                                "/home",
                                "/slistView",
                                "/sliprgList",
                                "/incomeStatement",
                                "/productList",
                                "/pSlip",
                                "/product",
                                "product",
                                "/logout",
                                "/IncomeStatement"
                        ).authenticated()  // 로그인한 사용자만 접근 가능
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index")
                        .usernameParameter("erpId")
                        .passwordParameter("erpPass")
                        .successHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession();
                            session.setAttribute("erpId", authentication.getName());
                            response.sendRedirect("/index");
                        })
                        .failureUrl("/?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );
        return http.build();
    }


}

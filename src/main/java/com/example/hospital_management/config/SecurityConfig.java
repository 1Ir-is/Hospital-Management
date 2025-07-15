package com.example.hospital_management.config;

import com.example.hospital_management.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Public access
                        .requestMatchers("/", "/home", "/css/**", "/js/**", "/assets/**",
                                "/images/**", "/favicon.ico").permitAll()

                        // Cho phép bệnh nhân truy cập các chức năng đặt lịch không cần đăng nhập
                        .requestMatchers("/patient/**").permitAll()

                        // Các chức năng phân quyền
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/department-head/**").hasAnyRole("DEPARTMENT_HEAD", "ADMIN")
                        .requestMatchers("/doctor/**").hasAnyRole("DOCTOR", "DEPARTMENT_HEAD", "ADMIN")
                        .requestMatchers("/nurse/**").hasAnyRole("NURSE", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")
                        .requestMatchers("/receptionist/**").hasAnyRole("RECEPTIONIST", "NURSE", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")
                        .requestMatchers("/lab-technician/**").hasAnyRole("LAB_TECHNICIAN", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")
                        .requestMatchers("/cashier/**").hasAnyRole("CASHIER", "RECEPTIONIST", "NURSE", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")
                        .requestMatchers("/pharmacy/**").hasAnyRole("PHARMACY_STAFF", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")
                        // Các API sensitive khác yêu cầu authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(customAuthenticationSuccessHandler())
                        .failureUrl("/?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            request.getRequestDispatcher("/error/403").forward(request, response);
                        })
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}

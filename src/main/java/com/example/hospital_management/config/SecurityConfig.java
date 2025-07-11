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

                        // === ROLE-BASED ACCESS ===

                        // ADMIN (Level 5) - Huy
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // DEPARTMENT_HEAD (Level 4) - Vương
                        .requestMatchers("/department-head/**").hasAnyRole("DEPARTMENT_HEAD", "ADMIN")

                        // DOCTOR (Level 3) - Vĩnh và Chung
                        .requestMatchers("/doctor/**").hasAnyRole("DOCTOR", "DEPARTMENT_HEAD", "ADMIN")

                        // NURSE (Level 2) - Khánh
                        .requestMatchers("/nurse/**").hasAnyRole("NURSE", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")

                        // RECEPTIONIST (Level 2) - Bình
                        .requestMatchers("/receptionist/**").hasAnyRole("RECEPTIONIST", "NURSE", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")

                        // LAB_TECHNICIAN - Nhơn
                        .requestMatchers("/lab-technician/**").hasAnyRole("LAB_TECHNICIAN", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")

                        // CASHIER - Nhơn
                        .requestMatchers("/cashier/**").hasAnyRole("CASHIER", "RECEPTIONIST", "NURSE", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")

                        // PHARMACY_STAFF - Nhơn
                        .requestMatchers("/pharmacy/**").hasAnyRole("PHARMACY_STAFF", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")

                        // PATIENT (Level 1) - Duy
                        .requestMatchers("/patient/**").hasAnyRole("PATIENT", "RECEPTIONIST", "NURSE", "DOCTOR", "DEPARTMENT_HEAD", "ADMIN")

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")  // ✅ Sử dụng homepage làm login page
                        .loginProcessingUrl("/login")  // ✅ URL xử lý login
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(customAuthenticationSuccessHandler())
                        .failureUrl("/?error=true")  // ✅ Redirect về homepage khi lỗi
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=true")  // ✅ Redirect về homepage sau logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
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

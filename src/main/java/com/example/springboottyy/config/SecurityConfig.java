package com.example.springboottyy.config;

import com.example.springboottyy.security.filter.JwtRequestFilter;
import com.example.springboottyy.security.handle.AuthenticationEntryPointHandler;
import com.example.springboottyy.security.handle.LogoutSuccessHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/13 0:00
 * @Version: 1.0
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationEntryPointHandler authenticationEntryPoint;
    private final LogoutSuccessHandlerImpl logoutSuccessHandler;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, AuthenticationEntryPointHandler authenticationEntryPoint, LogoutSuccessHandlerImpl logoutSuccessHandler, UserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/login","/register","/logs/").permitAll() // 允许所有人访问 "/public/**" 路径
//                        .requestMatchers("/api/role/**", "/api/menu/**").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/", "/**.html", "/**.css", "/**.js","/**.ico",
                                "/profile/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs",
                                "/v3/**", "/*/api-docs/**","/knife4j/**").permitAll()
                        .requestMatchers("/doc.html").permitAll()
                        .anyRequest().authenticated() // 其他请求需要身份验证
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(authenticationEntryPoint)
                )
                .logout(logout -> logout.
                        logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .permitAll()// 允许所有人访问登出功能
                )
                .csrf(AbstractHttpConfigurer::disable
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
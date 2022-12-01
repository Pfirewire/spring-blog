package com.codeup.springblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Security configuration for application
@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // Login configuration
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/posts")
                .permitAll()
                // Logout configuration
            .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                // Pages viewable without logging in
            .and()
                .authorizeHttpRequests()
                .antMatchers(
                        "/posts/create",
                        "/posts/{id}/edit",
                        "/profile",
                        "/posts/{id}/delete",
                        "/users/{id}"
                )
                .authenticated()
            .and()
                .authorizeHttpRequests()
                .antMatchers(
                        "/",
                        "/posts",
                        "/posts/{id}",
                        "/roll-dice"
                )
                .permitAll()
            .and()
                .httpBasic()
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

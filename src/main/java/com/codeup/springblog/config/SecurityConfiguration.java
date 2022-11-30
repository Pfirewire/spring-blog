package com.codeup.springblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Security configuration for application
@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
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
                .httpBasic()
        ;
        return http.build();
    }

//    private UserDetailsLoader usersLoader;
//
//    public SecurityConfiguration(UserDetailsLoader usersLoader) {
//        this.usersLoader = usersLoader;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(usersLoader)
//                .passwordEncoder(passwordEncoder())
//        ;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                // Login configuration
//            .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/posts")
//                .permitAll()
//                // Logout configuration
//            .and()
//                .logout()
//                .logoutSuccessUrl("/login?logout")
//                // Pages viewable without logging in
//            .and()
//                .authorizeRequests()
//                .antMatchers("/", "/posts", "/roll-dice")
//                .permitAll()
//                // Pages only viewable when logged in
//            .and()
//                .authorizeRequests()
//                .antMatchers(
//                        "/posts/create",
//                        "/posts/{id}/edit",
//                        "/profile",
//                        "/posts/{id}/delete",
//                        "/users/{id}"
//                )
//                .authenticated()
//        ;
//    }
}

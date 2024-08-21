package org.app.gtsconnected3.config;

import org.app.gtsconnected3.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableAsync
public class WebSecurityConfig {
    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/users/**").authenticated().
                                requestMatchers("/trip-img/**").permitAll().
                                requestMatchers("/css/**").permitAll()
                                .requestMatchers("/js/**").permitAll()
                                .anyRequest().permitAll()
                )
                .formLogin(login ->
                        login.loginPage("/login").permitAll()
                                .defaultSuccessUrl("/home")
                                .failureUrl("/login?error=true")
                )
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
                );

        return http.build();
    }


}

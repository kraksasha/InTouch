package com.example.intouch.Config;

import com.example.intouch.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeRequests()
                .requestMatchers("/myPage").authenticated()
                .requestMatchers("/myPhotos").authenticated()
                .requestMatchers("/myMusics").authenticated()
                .requestMatchers("/editMyPage").authenticated()
                .requestMatchers("/changePassword").authenticated()
                .requestMatchers("/changeAvatar/{id}").authenticated()
                .requestMatchers("/addFriend/{id}").authenticated()
                .requestMatchers("/myFriends").authenticated()
                .requestMatchers("/searchPerson").authenticated()
                .requestMatchers("/searchPersonFilter").authenticated()
                .requestMatchers("/addPhoto").authenticated()
                .requestMatchers("/deletePhoto/{id}").authenticated()
                .requestMatchers("/photo/{id}").authenticated()
                .requestMatchers("/addMusic").authenticated()
                .requestMatchers("/deleteMusic/{id}").authenticated()
                .requestMatchers("/downloadMusic/{id}").authenticated()
                .requestMatchers("/searchMusic").authenticated()
                .requestMatchers("/addLikeMusic/{id}").authenticated()
                .requestMatchers("/deleteLikeMusic/{id}").authenticated()
                .requestMatchers("/addLike/{id}").authenticated()
                .requestMatchers("/deleteLike/{id}").authenticated()
                .requestMatchers("/addComment/{id}").authenticated()
                .requestMatchers("/deleteComment/{id}").authenticated()
                .requestMatchers("/users").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

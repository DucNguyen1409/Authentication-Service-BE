package com.example.spring.authentication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.spring.authentication.common.Constant.LOG_OUT_URL;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    // white list
    private static final String[] AUTH_WHITE_LIST = { "/api/v1/auth/**" };

    private final JwtAuthentication jwtAuthentication;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults());

        // filter chain custom
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests()
                .requestMatchers(AUTH_WHITE_LIST) // some matcher no need to auth => permit all
                    .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthentication, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl(LOG_OUT_URL)
                .addLogoutHandler(logoutHandler) // logout configuration; this line will exec when call api /api/v1/auth/logout
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext())); // execute this filter before user-passwd authentication

        return httpSecurity.build();

    }

}

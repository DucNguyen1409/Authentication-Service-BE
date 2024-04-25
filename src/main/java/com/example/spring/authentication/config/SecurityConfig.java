package com.example.spring.authentication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.spring.authentication.common.Constant.*;
import static com.example.spring.authentication.entity.Permission.*;
import static com.example.spring.authentication.entity.Role.ADMIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
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
//                .requestMatchers("/**")
//                .permitAll()
                .requestMatchers(AUTH_WHITE_LIST) // some matcher no need to auth => permit all
                    .permitAll()
                .requestMatchers(ADMIN_API_URL).hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.GET, ADMIN_API_URL).hasAuthority(ADMIN_READ.name())
                .requestMatchers(HttpMethod.POST, ADMIN_API_URL).hasAuthority(ADMIN_CREATE.name())
                .requestMatchers(HttpMethod.PUT, ADMIN_API_URL).hasAuthority(ADMIN_UPDATE.name())
                .requestMatchers(HttpMethod.DELETE, ADMIN_API_URL).hasAuthority(ADMIN_DELETE.name())
//                .requestMatchers(HttpMethod.GET, USER_API_URL).hasAnyAuthority(USER_READ.name(), ADMIN_READ.name())
//                .requestMatchers(HttpMethod.POST, USER_API_URL).hasAuthority(USER_CREATE.name())
//                .requestMatchers(HttpMethod.PUT, USER_API_URL).hasAuthority(USER_UPDATE.name())
//                .requestMatchers(HttpMethod.DELETE, USER_API_URL).hasAnyAuthority(USER_DELETE.name())
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

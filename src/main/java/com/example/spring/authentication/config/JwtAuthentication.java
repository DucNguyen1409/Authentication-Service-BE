package com.example.spring.authentication.config;

import com.example.spring.authentication.common.Constant;
import com.example.spring.authentication.repository.TokenRepository;
import com.example.spring.authentication.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthentication extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(Constant.AUTHORIZATION);
        final String jwt;
        final String userEmail;

        // valid check token
        if (Objects.isNull(authHeader)
                || !authHeader.startsWith(Constant.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract token from header
        jwt = authHeader.substring(Constant.JWT_BEARER_INDEX);
        userEmail = jwtService.extractUserName(jwt);

        // valid check user exist
        if (Objects.isNull(userEmail)
                && Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            return;
        }

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        var isTokenActive = tokenRepository.findByToken(jwt)
                .map(t -> t.getExpired() && !t.getRevoked())
                .orElse(false);
        boolean tokenValid = jwtService.isTokenValid(jwt, userDetails);

        if (tokenValid && isTokenActive) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                                                .buildDetails(request));

            // update context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // do finalize the filter
        filterChain.doFilter(request, response);
    }

}

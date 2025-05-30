package com.spring.fastfood.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.fastfood.custom.CustomUserDetailService;
import com.spring.fastfood.enums.TokenType;
import com.spring.fastfood.exception.ErrorResponse;
import com.spring.fastfood.service.JwtService;
import com.spring.fastfood.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetail;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("{} {}", request.getMethod(), request.getRequestURI());

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("token: {}...", token.substring(0, Math.min(15, token.length())));

            try {
                String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
                log.info("username: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = userDetail.loadUserByUsername(username);

                    if (user != null) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken); // ✅ Sửa chỗ này
                        log.info("Authentication successful for user: {}", username);
                    } else {
                        log.error("User not found for username: {}", username);
                        respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, request.getRequestURI(), "User not found");
                        return;
                    }
                }

            } catch (AccessDeniedException e) {
                log.error("Access denied: {}", e.getMessage());
                respondWithError(response, HttpServletResponse.SC_FORBIDDEN, request.getRequestURI(), e.getMessage());
                return;
            } catch (Exception e) {
                log.error("Error extracting username: {}", e.getMessage());
                respondWithError(response, HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI(), "Invalid token");
                return;
            }
        }

        log.info("SecurityContext trước khi chuyển tiếp request: {}", SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);
    }

    private void respondWithError(HttpServletResponse response, int statusCode, String path, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse error = new ErrorResponse(path, message);
        new ObjectMapper().writeValue(response.getWriter(), error); // ✅ Sửa JSON response
    }
}

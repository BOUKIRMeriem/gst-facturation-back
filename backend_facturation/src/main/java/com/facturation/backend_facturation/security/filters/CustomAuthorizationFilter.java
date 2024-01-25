package com.facturation.backend_facturation.security.filters;

import com.facturation.backend_facturation.exceptions.responses.SecurityErrorResponse;
import com.facturation.backend_facturation.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthorizationFilter  extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if( request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/site/add")
                || request.getServletPath().equals("/api/account/token/refresh") || request.getServletPath().equals("/api/account/register") ){
            filterChain.doFilter(request, response);
        }else{
            String authorization = jwtUtil.extractTokenFromRequestHeader(request);
            if(authorization != null && authorization.startsWith("Bearer ")){
                try {
                    UsernamePasswordAuthenticationToken authenticationToken = jwtUtil.jwtVerifier(authorization);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                }catch (Exception exception){
                    HttpStatus status  = HttpStatus.UNAUTHORIZED;
                    SecurityErrorResponse errorResponse = SecurityErrorResponse.builder()
                            .status(String.valueOf(status))
                            .message(exception.getMessage())
                            .path(request.getRequestURI()).build();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    jwtUtil.mapper.writeValue(response.getOutputStream(), errorResponse);
                }
            }else {
                jwtUtil.tokenIsMissingResponse(request, response);
            }
        }
    }
}

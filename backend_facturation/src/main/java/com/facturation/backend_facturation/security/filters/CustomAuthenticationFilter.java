package com.facturation.backend_facturation.security.filters;

import com.facturation.backend_facturation.dto.AccountBasicInfoSecurityViewWithTokens;
import com.facturation.backend_facturation.exceptions.handlers.ErrorResponseHandler;
import com.facturation.backend_facturation.security.userDetails.UserDetailsService;
import com.facturation.backend_facturation.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final ErrorResponseHandler errorResponseHandler;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        log.info("Username is : {}", email); log.info("Password is : {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        try {
            return authenticationManager.authenticate(authenticationToken);
        }catch (AuthenticationException exception){
            errorResponseHandler.invalidCredentials(exception, request, response);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        AccountBasicInfoSecurityViewWithTokens basicUserInfo = userDetailsService.loadUsersBasicInfo(user.getUsername());
        log.info("Generating access and refresh tokens.");
        String access_token = jwtUtil.accessTokenGenerator( user, request, 10);
        String refresh_token = jwtUtil.refreshTokenGenerator( user, request, 24);
        basicUserInfo.setAccessToken(access_token);
        basicUserInfo.setRefreshToken(refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        jwtUtil.mapper.writeValue(response.getOutputStream(), basicUserInfo);
    }

}

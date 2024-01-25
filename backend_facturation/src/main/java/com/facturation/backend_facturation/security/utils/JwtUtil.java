package com.facturation.backend_facturation.security.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.facturation.backend_facturation.exceptions.responses.SecurityErrorResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.cookie.httpOnly}")
    private Boolean httpOnly;
    @Value("${jwt.cookie.secure}")
    private Boolean secure;
    @Value("${jwt.cookie.domain}")
    private String domain;

    public final ObjectMapper mapper;

    public UsernamePasswordAuthenticationToken jwtVerifier(String authorization){
        String token = authorization.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String email = decodedJWT.getSubject();
        String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }

    public String accessTokenGenerator(User user, HttpServletRequest request, long time){
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return  "Bearer " + JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ time * 3600 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

    }

    public String refreshTokenGenerator( User user, HttpServletRequest request, long time){
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return  "Bearer " + JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ time * 3600 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

    }

    private ResponseCookie generateCookieForToken(String name, String value, String path, String sameSite){
        return ResponseCookie.from(name, value)
                .httpOnly(httpOnly)
                .maxAge(Duration.ofHours(12))
                .secure(secure)
                .sameSite(sameSite)
                .path(path)
                .domain(domain).build();
    }


    public String extractTokenFromCookie(HttpServletRequest req, String cookieName) {
        return stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public String extractTokenFromRequestHeader(HttpServletRequest req){
        return req.getHeader("Authorization");
    }
    public String extractRTokenFromRequestHeader(HttpServletRequest req){
        return req.getHeader("Authorization-r");
    }

    public String refreshTokenVerifier(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String authorizationHeader = extractRTokenFromRequestHeader(req);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refresh_token = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            return decodedJWT.getSubject();
        }
        this.tokenIsMissingResponse(req, response);
        return null;
    }
    public void generateAccessTokenWhenExpired(HttpServletRequest req, HttpServletResponse res, UserDetails userDetails) throws IOException {
        User user = new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        String accessToken = accessTokenGenerator(user, req, 10);
        Map<String, String> token = new HashMap<>();
        token.put("accessToken", accessToken);
        res.setContentType(APPLICATION_JSON_VALUE);
        mapper.writeValue(res.getOutputStream(), token);
    }

    public void tokenIsMissingResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityErrorResponse errorResponse = SecurityErrorResponse.builder()
                .status(String.valueOf(HttpStatus.UNAUTHORIZED))
                .message("Token is missing.")
                .path(request.getRequestURI()).build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}

package com.facturation.backend_facturation.security.config;


import com.facturation.backend_facturation.exceptions.handlers.CustomAccessDeniedHandler;
import com.facturation.backend_facturation.exceptions.handlers.ErrorResponseHandler;
import com.facturation.backend_facturation.security.filters.CustomAuthenticationFilter;
import com.facturation.backend_facturation.security.filters.CustomAuthorizationFilter;
import com.facturation.backend_facturation.security.userDetails.UserDetailsService;
import com.facturation.backend_facturation.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    private final ErrorResponseHandler errorResponseHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(authenticationConfiguration), userDetailsService, jwtUtil, errorResponseHandler);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http
                .csrf()
                .disable()
                .cors()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler(errorResponseHandler))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/login/**","/api/account/register/**","/api/site/add/**", "/api/account/token/refresh/**").permitAll()
                .and()
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public WebMvcConfigurer cors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins( "*" )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Origin", "Access-Control-Allow-Origin", "Content-Type",
                                "Accept", "Authorization", "Authorization-r", "X-Requested-With",
                                "Access-Control-Request-Method", "Access-Control-Request-Headers");
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ErrorResponseHandler err) {
        return new CustomAccessDeniedHandler(err);
    }

}

package com.facturation.backend_facturation.security.injectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.facturation.backend_facturation.exceptions.handlers.ErrorResponseHandler;
import com.facturation.backend_facturation.security.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityBeansInjector {

    /*
     * Creating a Bean for passwordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Creating a Bean for JwtUtil.
     */
    @Bean
    public JwtUtil jwtHelperBean(){return new JwtUtil(objectMapper());}

    /*
     * Creating a Bean for ErrorResponseHandler.
     */
    @Bean
    public ErrorResponseHandler ErrorResponseHandlerBean(){return new ErrorResponseHandler(objectMapper());}

    /*
     * Creating an ObjectMapper with jackson modules {parameter names | date/time | dataTypes}.
     */
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
    }
}

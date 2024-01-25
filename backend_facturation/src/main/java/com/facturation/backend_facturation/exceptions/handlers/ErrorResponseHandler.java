package com.facturation.backend_facturation.exceptions.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.facturation.backend_facturation.exceptions.responses.SecurityErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class ErrorResponseHandler {

    private final ObjectMapper mapper;
    public void accessDeniedHandler(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        generateErrorResponse(exception, request, response);
    }

    public void invalidCredentials(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        generateErrorResponse(exception, request, response);
    }

    private void generateErrorResponse(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status  = HttpStatus.UNAUTHORIZED;
        SecurityErrorResponse errorResponse = SecurityErrorResponse.builder()
                .status(String.valueOf(status))
                .message(exception.getMessage())
                .path(request.getRequestURI()).build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            mapper.writeValue(response.getOutputStream(), errorResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

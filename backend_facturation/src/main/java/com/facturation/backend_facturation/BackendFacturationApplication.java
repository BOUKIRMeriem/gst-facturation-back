package com.facturation.backend_facturation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BackendFacturationApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BackendFacturationApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(BackendFacturationApplication.class);
    }
}

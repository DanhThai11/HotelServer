package com.example.rexhotel.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnvConfig {
    private final Dotenv dotenv;

    @Autowired
    public EnvConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    public String get(String key) {
        return dotenv.get(key);
    }
} 
package com.example.rexhotel.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PropertyConfig implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(PropertyConfig.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (event.getApplicationContext().getEnvironment() instanceof ConfigurableEnvironment) {
                ConfigurableEnvironment environment = (ConfigurableEnvironment) event.getApplicationContext().getEnvironment();
                Dotenv dotenv = Dotenv.load();
                
                Map<String, Object> envProperties = new HashMap<>();
                dotenv.entries().forEach(entry -> envProperties.put(entry.getKey(), entry.getValue()));
                
                logger.info("Loaded environment variables: {}", envProperties.keySet());
                
                MapPropertySource propertySource = new MapPropertySource("dotenvProperties", envProperties);
                environment.getPropertySources().addFirst(propertySource);
                
                logger.info("Environment variables loaded successfully");
            }
        } catch (Exception e) {
            logger.error("Error loading environment variables", e);
        }
    }
} 
package com.jjk.env_test.springinit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentConfiguration {

    @Value("${app.component:componentA}")
    private String componentType;

    @Bean
    public Runnable componentRunner(ComponentA componentA, ComponentB componentB) {
        if ("componentA".equalsIgnoreCase(componentType)) {
            return () -> componentA.execute();
        } else if ("componentB".equalsIgnoreCase(componentType)) {
            return () -> componentB.execute();
        } else {
            throw new IllegalArgumentException("Unknown component type: " + componentType);
        }
    }
}

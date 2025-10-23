package com.jjk.env_test.springinit;

import org.springframework.stereotype.Component;

@Component
public class ComponentB {
    public void execute() {
        System.out.println("Component B executed");
    }
}

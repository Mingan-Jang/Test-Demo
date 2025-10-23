package com.jjk.env_test.springinit;

import org.springframework.stereotype.Component;

@Component
public class ComponentA {
    public void execute() {
        System.out.println("Component A executed");
    }
}


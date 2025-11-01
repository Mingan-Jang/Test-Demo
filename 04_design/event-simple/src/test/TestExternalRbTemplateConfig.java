package com.event.test;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestExternalRbTemplateConfig {
    @Bean("testExternalRabbitTemplate")
    public RabbitTemplate testExternalRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(true);

        // ReturnCallback
        template.setReturnsCallback(returned -> {
            System.err.println("[測試] 消息被退回: " + returned);
            // 可在此記錄測試用退回訊息
        });

        // ConfirmCallback
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.err.println("[測試] 消息未確認: " + cause);
                // 可在此記錄測試用未確認訊息
            }
        });

        return template;
    }
}

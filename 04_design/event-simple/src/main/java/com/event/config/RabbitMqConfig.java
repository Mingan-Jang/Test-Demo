package com.event.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
	// 分別 RoutingKey
	public static final String NO_EXIST_QUEUE = "no.exist.queue";

	public static final String PROCESS_DELAY_EXCHANGE = "event.process.delay";
	public static final String PROCESS_DELAY_QUEUE = "event.process.queue";
	public static final String PROCESS_CREATE_ROUTING_KEY = "event.process.create";
	public static final String PROCESS_UPDATE_ROUTING_KEY = "event.process.update";

	public static final String BROADCAST_EXCHANGE = "event.broadcast.fanout";
	public static final String BROADCAST_QUEUE = "event.broadcast.queue";

	public static final String DLX_EXCHANGE = "event.dlx.direct";
	public static final String DLX_QUEUE = "event.dlx.queue";
	// 主要失敗模組，綁定PROCESS模組的死信使用
	// ## 自動死信使用(basic.reject/basiic.nack)
	public static final String DLX_ROUTING_KEY_AUTO = "event.dlx.auto";
	// ## 手動送入死信使用(rabbitTemplate.convertAndSend(dlx_exchange, dlx_routingkey, ...))
	public static final String DLX_ROUTING_KEY_MANUAL = "event.dlx.maunal";

	// ===========================
	// 📦 Process 模組 (Direct Exchange)
	// ===========================

	@Bean
	public CustomExchange processExchange() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-delayed-type", "direct"); // 底層行為類似 direct
		return new CustomExchange(PROCESS_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
	}

	// # 主處理隊列，綁定 DLX 自動死信交換器
	@Bean
	public Queue processQueue() {
		return QueueBuilder.durable(PROCESS_DELAY_QUEUE).withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
				.withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY_AUTO).build();
	}

	@Bean
	public Binding processCreateBinding() {
		return BindingBuilder.bind(processQueue()).to(processExchange()).with(PROCESS_CREATE_ROUTING_KEY).noargs();
	}

	@Bean
	public Binding processUpdateBinding() {
		return BindingBuilder.bind(processQueue()).to(processExchange()).with(PROCESS_UPDATE_ROUTING_KEY).noargs();
	}

	// ===========================
	// 📢 Broadcast 模組 (Fanout Exchange)
	// ===========================

	@Bean
	public FanoutExchange broadcastExchange() {
		return new FanoutExchange(BROADCAST_EXCHANGE, true, false);
	}

	@Bean
	public Queue broadcastQueue() {
		return QueueBuilder.durable(BROADCAST_QUEUE).build();
	}

	@Bean
	public Binding broadcastBinding() {
		return BindingBuilder.bind(broadcastQueue()).to(broadcastExchange());
	}

	// ===========================
	// 💀 DLX 模組 (Dead Letter Direct Exchange)
	// ===========================

	@Bean
	public DirectExchange deadLetterExchange() {
		return new DirectExchange(DLX_EXCHANGE, true, false);
	}

	@Bean
	public Queue deadLetterQueue() {
		return QueueBuilder.durable(DLX_QUEUE).build();
	}

	@Bean
	public Binding deadLetterAutoBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLX_ROUTING_KEY_AUTO);
	}

	@Bean
	public Binding deadLetterManualBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLX_ROUTING_KEY_MANUAL);
	}

	// ===========================
	// ⚙️ 通用設定
	// ===========================
	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(converter);

		// 自動生成 Message ID
		template.setBeforePublishPostProcessors(message -> {
			MessageProperties props = message.getMessageProperties();
			if (props.getMessageId() == null) {
				props.setMessageId(
						System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 8));
			}
			return message;
		});

		// 發送確認回調（可用於監控）
		template.setConfirmCallback((correlationData, ack, cause) -> {
			if (!ack) {
				System.err.println("❌ RabbitMQ 發送失敗: " + cause);
			}
		});

		return template;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
			Jackson2JsonMessageConverter converter) {

		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(converter);
		return factory;
	}
}

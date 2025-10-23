package com.jjk.demo.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.jjk.demo.trigger.ReceivedEvent;

import jakarta.annotation.Resource;

@Configuration
public class MqttConfiguration {

	@Resource
	private ApplicationContext applicationContext;

	private String userName = "iisi";
	private String password = "1qaz!QAZ";
	private String[] serverURIs = { "tcp://127.0.0.1:1883" };
	public static final String REVISE_EVENT_TOPIC = "RailCloud/RSM/WS/ReviseEvent";

	@Bean
	public MqttConnectOptions mqttConnectOptions() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName(userName);
		options.setPassword(password.toCharArray());
		options.setServerURIs(serverURIs);
		options.setCleanSession(false);
		options.setAutomaticReconnect(true);
		return options;
	}

	@Bean(name = "mqttInboundChannel")
	public MessageChannel mqttInboundChannel() {
		return new DirectChannel();
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(mqttConnectOptions());
		return factory;
	}

	@Bean
	public MessageProducer consumer() {
		String clientId = "CONSUMER_" + System.currentTimeMillis();

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId,
				mqttClientFactory(), REVISE_EVENT_TOPIC);
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInboundChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInboundChannel")
	public MessageHandler myHandler() {
		return message -> {
			System.out.println("Received" + message.getPayload());
			String applicationMsg = "Received";
			ReceivedEvent e = new ReceivedEvent(applicationMsg);
			applicationContext.publishEvent(e);
		};
	}

//	@Bean
//	public IntegrationFlow mqttInboundFlow() {
//		return IntegrationFlow.from(mqttInboundChannel()).handle(myHandler()).get();
//	}
}

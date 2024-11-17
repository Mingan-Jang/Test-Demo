package com.jjk.demo.maintest;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PublishSample {

	public static void main(String[] args) {
		String broker = "tcp://127.0.0.1:1883";
		String topic = "mqtt/meowmeow";
		String username = "jjk";
		String password = "123456";
		String clientid = "publish_client";
		String content = "Hello MQTT_V4";
		int qos = 0;

		try

		{
			MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
			// 連接參數
			MqttConnectOptions options = new MqttConnectOptions();
			// 設置使用者名稱和密碼
			options.setUserName(username);
			options.setPassword(password.toCharArray());
			// 設置連接超時時間（秒）
			options.setConnectionTimeout(60);
			// 設置心跳間隔時間（秒）
			options.setKeepAliveInterval(60);

			// (1) 一次性地發送
			// 連接到 MQTT 伺服器
			client.connect(options);

			// 創建消息並設置 QoS（服務質量）
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);

			// 發布消息到指定的主題
			client.publish(topic, message);
			System.out.println("Message published");
			System.out.println("topic: " + topic);
			System.out.println("message content: " + content);

			// 斷開與 MQTT 伺服器的連接
			client.disconnect();

			// 關閉 MQTT 客戶端
			client.close();

			// (2) 設置回調
//			client.setCallback(new MqttCallback() {
//
//				public void connectionLost(Throwable cause) {
//					System.out.println("connectionLost: " + cause.getMessage());
//				}
//
//				// For subscriber
//				public void messageArrived(String topic, MqttMessage message) {
//					System.out.println("topic: " + topic);
//					System.out.println("Qos: " + message.getQos());
//					System.out.println("message content: " + new String(message.getPayload()));
//
//				}
//
//				// For publisher
//				public void deliveryComplete(IMqttDeliveryToken token) {
//					System.out.println("deliveryComplete---------" + token.isComplete());
//				}
//
//			});
//			client.connect(options);
//			MqttMessage message = new MqttMessage(content.getBytes());
//			message.setQos(qos);
//			client.publish(topic, message);
//			System.out.println("Message published");
//			System.out.println("topic: " + topic);
//			System.out.println("message content: " + content);
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

}

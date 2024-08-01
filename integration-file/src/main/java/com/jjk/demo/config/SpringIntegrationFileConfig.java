package com.jjk.demo.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.BridgeFrom;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
public class SpringIntegrationFileConfig {
	public final String INPUT_DIR = "D:\\readfile\\in";
	public final String OUTPUT_DIR = "D:\\readfile\\out";
	
	public final String OUTPUT_DIR1 = "D:\\readfile\\out1";

	public final String OUTPUT_DIR2 = "D:\\readfile\\out2";

	public final String OUTPUT_DIR3 = "D:\\readfile\\out3";

	public final String FILE_PATTERN = "*.txt";

	@Bean(name = "fileChannel")
	public MessageChannel fileChannel() {
		return new DirectChannel();
	}

	// InboundChannelAdapter>>這是一個Spring Integration的註解，用於指示該方法是一個入站通道適配器。入站通道適配器用於從外部系統或資料來源將數據引入Spring Integration流。
	// value = "fileChannel": 指定了該入站通道適配器將數據放置在名為"fileChannel"的通道中。這意味著該通道將接收來自適配器的數據。
	// poller = @Poller(fixedDelay = "1000"): 這個設定指定了一個輪詢器（Poller），用於定期從資料來源（在這裡是文件通道）讀取數據。fixedDelay = "1000"表示每1000毫秒（即1秒）輪詢一次。
	@Bean
	@InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "1000"))
	public MessageSource<File> fileReadingMessageSource() {
		FileReadingMessageSource sourceReader = new FileReadingMessageSource();
		sourceReader.setDirectory(new File(INPUT_DIR));
		sourceReader.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));
		return sourceReader;
	}

	@Bean
	@ServiceActivator(inputChannel = "fileChannel")
	public MessageHandler fileWritingMessageHandler() {
		FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR));
		handler.setFileExistsMode(FileExistsMode.REPLACE);
		handler.setExpectReply(false);
		return handler;
	}
	
	
	

}
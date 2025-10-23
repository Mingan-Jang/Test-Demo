package com.jjk.demo;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.jjk.demo.trigger.ReceivedEvent;

@Service
public class ReceivedEventListener implements ApplicationListener<ReceivedEvent> {

	
	@Override
    public void onApplicationEvent(ReceivedEvent event) {
        Object source = event.getSource();

        // 调用接收事件方法
        System.out.println("Success");
    }
}

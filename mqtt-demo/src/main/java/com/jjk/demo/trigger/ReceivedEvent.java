package com.jjk.demo.trigger;

import org.springframework.context.ApplicationEvent;

public class ReceivedEvent extends ApplicationEvent {

	public ReceivedEvent(Object source) {
		super(source);
	}
}

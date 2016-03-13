package com.mq.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

@Service(value="myMessageListener")
public class MyMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		System.out.println("received message is :"+message.toString());
	}

}

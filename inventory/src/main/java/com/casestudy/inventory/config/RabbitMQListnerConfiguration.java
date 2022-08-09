package com.casestudy.inventory.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.casestudy.instagrocer.commons.RabbitMQMessageListner;
import com.casestudy.instagrocer.commons.constant.InstaGrocerEvent;

@Configuration
public class RabbitMQListnerConfiguration {

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter adapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(adapter);
		container.setQueueNames(InstaGrocerEvent.ORDER_CREATED);
		return container;
	}

	@Bean
	public MessageListenerAdapter listenerAdapter(RabbitMQMessageListner listner) {
		return new MessageListenerAdapter(listner, "onMessageReceive");
	}

}

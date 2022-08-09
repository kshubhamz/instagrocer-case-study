package com.casestudy.order.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.casestudy.instagrocer.commons.RabbitMQMessageListner;
import com.casestudy.instagrocer.commons.constant.InstaGrocerEvent;

@Configuration
public class RabbitMQListnerConfiguration {
	
	private static final String LISTNER_METHOD = "onMessageReceive";
	
	@Bean
	MessageListenerAdapter orderCompletedMessageListner(@Qualifier("OrderCompletedListner") RabbitMQMessageListner messageListner) {
		return new MessageListenerAdapter(messageListner, LISTNER_METHOD);
	}
	
	@Bean
	MessageListenerAdapter orderFailedMessageListner(@Qualifier("OrderFailedListner") RabbitMQMessageListner messageListner) {
		return new MessageListenerAdapter(messageListner, LISTNER_METHOD);
	}
	
	@Bean
	MessageListenerAdapter orderProductUnAvailableMessageListner(@Qualifier("ProductOrderUnAvailableListner") RabbitMQMessageListner messageListner) {
		return new MessageListenerAdapter(messageListner, LISTNER_METHOD);
	}
	
	@Bean
	SimpleMessageListenerContainer orderCompletedMessageListnerContainer(ConnectionFactory connectionFactory,
			@Qualifier("orderCompletedMessageListner") MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(listenerAdapter);
		container.setQueueNames(InstaGrocerEvent.ORDER_SUCCESS);
		
		return container;
	}
	
	@Bean
	SimpleMessageListenerContainer orderFailedMessageListnerContainer(ConnectionFactory connectionFactory,
			@Qualifier("orderFailedMessageListner") MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(listenerAdapter);
		container.setQueueNames(InstaGrocerEvent.ORDER_FAILURE);
		
		return container;
	}
	
	@Bean
	SimpleMessageListenerContainer orderProductUnAvailableMessageListnerContainer(ConnectionFactory connectionFactory,
			@Qualifier("orderProductUnAvailableMessageListner") MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(listenerAdapter);
		container.setQueueNames(InstaGrocerEvent.PRODUCT_SOME_ORDER_UNAVAILABLE);
		
		return container;
	}
}

package com.casestudy.catalogue.config;

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
	
	private static final String RABBIT_LISTNER_METHOD = "onMessageReceive";
	
	@Bean
	SimpleMessageListenerContainer inventoryItemCreatedListnerContainer(ConnectionFactory connectionFactory, 
			@Qualifier("inventoryItemCreatedListnerAdapter") MessageListenerAdapter adapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(adapter);
		container.setQueueNames(InstaGrocerEvent.INVENTORY_ITEM_CREATED);
		return container;
	}
	
	@Bean
	SimpleMessageListenerContainer inventoryItemUpdatedListnerContainer(ConnectionFactory connectionFactory, 
			@Qualifier("inventoryItemUpdatedListnerAdapter") MessageListenerAdapter adapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(adapter);
		container.setQueueNames(InstaGrocerEvent.INVENTORY_ITEM_UPDATED);
		return container;
	}
	
	@Bean
	SimpleMessageListenerContainer inventoryItemDeletedListnerContainer(ConnectionFactory connectionFactory, 
			@Qualifier("inventoryItemDeletedListnerAdapter") MessageListenerAdapter adapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(adapter);
		container.setQueueNames(InstaGrocerEvent.INVENTORY_ITEM_DELETED);
		return container;
	}
	
	@Bean
	MessageListenerAdapter inventoryItemCreatedListnerAdapter(@Qualifier("inventoryItemCreatedListner") RabbitMQMessageListner messageListner) {
		return new MessageListenerAdapter(messageListner, RABBIT_LISTNER_METHOD);
	}
	
	@Bean
	MessageListenerAdapter inventoryItemUpdatedListnerAdapter(@Qualifier("inventoryItemUpdatedListner") RabbitMQMessageListner messageListner) {
		return new MessageListenerAdapter(messageListner, RABBIT_LISTNER_METHOD);
	}
	
	@Bean
	MessageListenerAdapter inventoryItemDeletedListnerAdapter(@Qualifier("inventoryItemDeletedListner") RabbitMQMessageListner messageListner) {
		return new MessageListenerAdapter(messageListner, RABBIT_LISTNER_METHOD);
	}

}

package com.casestudy.inventory.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.casestudy.instagrocer.commons.constant.InstaGrocerEvent;
import com.casestudy.instagrocer.commons.constant.InstaGrocerQueueBean;
import com.casestudy.instagrocer.commons.constant.InstaGrocerQueueBindingBean;
import com.casestudy.instagrocer.commons.constant.InstaGrocerRouting;
import com.casestudy.instagrocer.commons.constant.InstaGrocerTopicExchange;

@Configuration
public class RabbitMQPublisherConfiguration {
	@Value("${rabbitmq.queue.dead-letter}")
	private String deadLetterQueue;
	
	@Bean
	TopicExchange inventoryTopicExchange() {
		return new TopicExchange(InstaGrocerTopicExchange.INVENTORY);
	}
	
	@Bean
	TopicExchange orderTopicExchange() {
		return new TopicExchange(InstaGrocerTopicExchange.ORDER);
	}
	
	@Bean(InstaGrocerQueueBean.INVENTORY_ITEM_CREATED)
	Queue inventoryItemCreatedQueue() {
		return QueueBuilder.durable(InstaGrocerEvent.INVENTORY_ITEM_CREATED)
			      .withArgument("x-dead-letter-exchange", "")
			      .withArgument("x-dead-letter-routing-key", deadLetterQueue)
			      .build();
		
		// return new Queue(InstaGrocerEvent.INVENTORY_ITEM_CREATED, false);
	}
	
	@Bean(InstaGrocerQueueBean.INVENTORY_ITEM_UPDATED)
	Queue inventoryItemUpdatedQueue() {
		return QueueBuilder.durable(InstaGrocerEvent.INVENTORY_ITEM_UPDATED)
			      .withArgument("x-dead-letter-exchange", "")
			      .withArgument("x-dead-letter-routing-key", deadLetterQueue)
			      .build();
		
		// return new Queue(InstaGrocerEvent.INVENTORY_ITEM_UPDATED, false);
	}
	
	@Bean(InstaGrocerQueueBean.INVENTORY_ITEM_DELETED)
	Queue inventoryItemDeletedQueue() {
		return QueueBuilder.durable(InstaGrocerEvent.INVENTORY_ITEM_DELETED)
			      .withArgument("x-dead-letter-exchange", "")
			      .withArgument("x-dead-letter-routing-key", deadLetterQueue)
			      .build();
		
		// return new Queue(InstaGrocerEvent.INVENTORY_ITEM_DELETED, false);
	}
	
	@Bean(InstaGrocerQueueBean.ORDER_FAILURE)
	Queue orderCancelledQueue() {
		return QueueBuilder.durable(InstaGrocerEvent.ORDER_FAILURE)
				.withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", deadLetterQueue)
				.build();

		// return new Queue(InstaGrocerEvent.ORDER_CREATED, false);
	}
	
	@Bean(InstaGrocerQueueBean.ORDER_SUCCESS)
	Queue orderCompletedQueue() {
		return QueueBuilder.durable(InstaGrocerEvent.ORDER_SUCCESS)
				.withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", deadLetterQueue)
				.build();

		// return new Queue(InstaGrocerEvent.ORDER_CREATED, false);
	}
	
	@Bean(InstaGrocerQueueBean.ORDER_UNAVAILABLE_PRODUCT)
	Queue orderUnavailableProductQueue() {
		return QueueBuilder.durable(InstaGrocerEvent.PRODUCT_SOME_ORDER_UNAVAILABLE)
				.withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", deadLetterQueue)
				.build();

		// return new Queue(InstaGrocerEvent.ORDER_CREATED, false);
	}
	
	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable(deadLetterQueue).build();
	}
	
	@Bean
	Binding inventoryItemCreatedBinding(@Qualifier("inventoryTopicExchange") TopicExchange topicExchange,
			@Qualifier(InstaGrocerQueueBean.INVENTORY_ITEM_CREATED) Queue queue) {
		return BindingBuilder.bind(queue).to(topicExchange).with(InstaGrocerRouting.INVENTORY_ITEM_CREATE_ROUTE);
	}
	
	@Bean
	Binding inventoryItemUpdatedBinding(@Qualifier("inventoryTopicExchange") TopicExchange topicExchange,
			@Qualifier(InstaGrocerQueueBean.INVENTORY_ITEM_UPDATED) Queue queue) {
		return BindingBuilder.bind(queue).to(topicExchange).with(InstaGrocerRouting.INVENTORY_ITEM_UPDATE_ROUTE);
	}
	
	@Bean
	Binding inventoryItemDeletedBinding(@Qualifier("inventoryTopicExchange") TopicExchange topicExchange,
			@Qualifier(InstaGrocerQueueBean.INVENTORY_ITEM_DELETED) Queue queue) {
		return BindingBuilder.bind(queue).to(topicExchange).with(InstaGrocerRouting.INVENTORY_ITEM_DELETE_ROUTE);
	}
	
	@Bean(InstaGrocerQueueBindingBean.ORDER_FAILURE)
	Binding orderFailureBinding(@Qualifier("orderTopicExchange") TopicExchange exchange,
			@Qualifier(InstaGrocerQueueBean.ORDER_FAILURE) Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(InstaGrocerRouting.ORDER_FAILURE);
	}
	
	@Bean(InstaGrocerQueueBindingBean.ORDER_SUCCESS)
	Binding orderSuccessBinding(@Qualifier("orderTopicExchange") TopicExchange exchange,
			@Qualifier(InstaGrocerQueueBean.ORDER_SUCCESS) Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(InstaGrocerRouting.ORDER_SUCCESS);
	}
	
	@Bean
	Binding orderProductUnavailableBinding(@Qualifier("orderTopicExchange") TopicExchange exchange,
			@Qualifier(InstaGrocerQueueBean.ORDER_UNAVAILABLE_PRODUCT) Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(InstaGrocerRouting.PRODUCT_ORDER_SOME_UNAVAILABLE_ROUTE);
	}
}

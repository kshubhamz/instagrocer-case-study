package com.casestudy.order.config;

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

	@Bean(InstaGrocerQueueBean.ORDER_CREATED)
	public Queue orderCreatedQueue() {
		return QueueBuilder.durable(InstaGrocerEvent.ORDER_CREATED)
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
	public TopicExchange orderTopicExchange() {
		return new TopicExchange(InstaGrocerTopicExchange.ORDER);
	}

	@Bean(InstaGrocerQueueBindingBean.ORDER_CREATED)
	public Binding orderCreatedBinding(@Qualifier("orderTopicExchange") TopicExchange exchange,
			@Qualifier(InstaGrocerQueueBean.ORDER_CREATED) Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(InstaGrocerRouting.ORDER_CREATED);
	}
}

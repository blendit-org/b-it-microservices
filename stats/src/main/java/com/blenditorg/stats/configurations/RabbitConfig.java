package com.blenditorg.stats.configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	public static final String RENDER_STATS_QUEUE = "renderStatsQueue";
	
	@Bean
    public Queue renderStatsQueue() {  // new queue bean
        return QueueBuilder.durable(RENDER_STATS_QUEUE).build();
    }
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
}
package com.blenditorg.stats.services;

import java.time.LocalDate;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.blenditorg.stats.repositories.StatsRepository;
import com.blenditorg.stats.configurations.RabbitConfig;
import com.blenditorg.stats.dtos.FrameMessage;
import com.blenditorg.stats.entities.RenderedFramesSummary;
@Service
public class ListenerService {
	
	private final StatsRepository repository;

	public ListenerService(StatsRepository repository) {
		super();
		this.repository = repository;
	}
	
	@RabbitListener(queues = RabbitConfig.RENDER_STATS_QUEUE)
	public void handleRenserStats(FrameMessage message) {
		System.out.println("got a message " + message);
		try {
			LocalDate day = message.getRenderedDay();
			repository.findById(day).map(summary -> {
				summary.setFrameCount(summary.getFrameCount() + 1);
				return repository.save(summary);
			}).orElseGet(() -> {
				RenderedFramesSummary summary = new RenderedFramesSummary();
				summary.setDay(day);
				summary.setFrameCount(1);
				return repository.save(summary);
			});
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
	}
}

package com.blenditorg.stats.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.stats.dtos.DailyRenderDto;
import com.blenditorg.stats.dtos.RenderStatsDto;
import com.blenditorg.stats.repositories.StatsRepository;



@RestController
@RequestMapping("/stats")
public class StatsController {
	
	private final StatsRepository statsRepository;
	
	
	public StatsController(StatsRepository statsRepository) {
		super();
		this.statsRepository = statsRepository;
	}




	@GetMapping("/general")
	public ResponseEntity<RenderStatsDto> getStats() {
		Object[] result = (Object[]) statsRepository.getRenderStatsRaw();
        Long totalFrames = result[0] != null ? ((Number) result[0]).longValue() : 0L;
        Long last7Days   = result[1] != null ? ((Number) result[1]).longValue() : 0L;
        Long last30Days  = result[2] != null ? ((Number) result[2]).longValue() : 0L;

        List<DailyRenderDto> last7Breakdown = statsRepository.getLast7DaysBreakdown()
            .stream()
            .map(r -> new DailyRenderDto(
                ((java.sql.Date) r[0]).toLocalDate(),
                ((Number) r[1]).longValue()))
            .toList();

        List<DailyRenderDto> last30Breakdown = statsRepository.getLast30DaysBreakdown()
            .stream()
            .map(r -> new DailyRenderDto(
                ((java.sql.Date) r[0]).toLocalDate(),
                ((Number) r[1]).longValue()))
            .toList();

        return ResponseEntity.ok(new RenderStatsDto(totalFrames, last7Days, last30Days, last7Breakdown, last30Breakdown));
		
	}
}
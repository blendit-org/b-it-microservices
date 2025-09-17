package com.blenditorg.stats.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blenditorg.stats.entities.RenderedFramesSummary;

public interface StatsRepository extends JpaRepository<RenderedFramesSummary, LocalDate> {
	@Query(value = """
	        SELECT 
	            SUM(frame_count) AS totalFrames,
	            SUM(CASE WHEN day >= CURDATE() - INTERVAL 6 DAY THEN frame_count ELSE 0 END) AS last7Days,
	            SUM(CASE WHEN day >= CURDATE() - INTERVAL 29 DAY THEN frame_count ELSE 0 END) AS last30Days
	        FROM rendered_frames_summary
	        """, nativeQuery = true)
	Object getRenderStatsRaw();
	
	// Per-day for last 7 days
    @Query(value = """
        SELECT day, frame_count
        FROM rendered_frames_summary
        WHERE day >= CURDATE() - INTERVAL 6 DAY
        ORDER BY day
        """, nativeQuery = true)
    List<Object[]> getLast7DaysBreakdown();

    // Per-day for last 30 days
    @Query(value = """
        SELECT day, frame_count
        FROM rendered_frames_summary
        WHERE day >= CURDATE() - INTERVAL 29 DAY
        ORDER BY day
        """, nativeQuery = true)
    List<Object[]> getLast30DaysBreakdown();
}

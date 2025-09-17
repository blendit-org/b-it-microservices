package com.blenditorg.stats.dtos;

import java.util.List;

public class RenderStatsDto {
    private Long totalFrames;
    private Long last7Days;
    private Long last30Days;
    private List<DailyRenderDto> last7DaysBreakdown;
    private List<DailyRenderDto> last30DaysBreakdown;

    public RenderStatsDto(Long totalFrames, Long last7Days, Long last30Days,
                          List<DailyRenderDto> last7DaysBreakdown,
                          List<DailyRenderDto> last30DaysBreakdown) {
        this.totalFrames = totalFrames;
        this.last7Days = last7Days;
        this.last30Days = last30Days;
        this.last7DaysBreakdown = last7DaysBreakdown;
        this.last30DaysBreakdown = last30DaysBreakdown;
    }

    public Long getTotalFrames() { return totalFrames; }
    public Long getLast7Days() { return last7Days; }
    public Long getLast30Days() { return last30Days; }
    public List<DailyRenderDto> getLast7DaysBreakdown() { return last7DaysBreakdown; }
    public List<DailyRenderDto> getLast30DaysBreakdown() { return last30DaysBreakdown; }
}

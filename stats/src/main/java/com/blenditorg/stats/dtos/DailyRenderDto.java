package com.blenditorg.stats.dtos;

import java.time.LocalDate;

public class DailyRenderDto {
    private LocalDate day;
    private Long frameCount;

    public DailyRenderDto(LocalDate day, Long frameCount) {
        this.day = day;
        this.frameCount = frameCount;
    }

    public LocalDate getDay() { return day; }
    public Long getFrameCount() { return frameCount; }
}


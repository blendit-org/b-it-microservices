package com.blenditorg.stats.dtos;

import java.time.LocalDate;

public class FrameMessage {
    private LocalDate renderedDay; // just the date

    public FrameMessage() {}

    public FrameMessage(LocalDate renderedDay) {
        this.renderedDay = renderedDay;
    }

    public LocalDate getRenderedDay() {
        return renderedDay;
    }

    public void setRenderedDay(LocalDate renderedDay) {
        this.renderedDay = renderedDay;
    }
}

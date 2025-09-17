package com.blenditorg.stats.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rendered_frames_summary")
public class RenderedFramesSummary {
    @Id
    private LocalDate day;

    private long frameCount;

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public long getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(long frameCount) {
		this.frameCount = frameCount;
	}

    // getters and setters
}

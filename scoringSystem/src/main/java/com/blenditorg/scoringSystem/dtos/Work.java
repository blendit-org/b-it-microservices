package com.blenditorg.scoringSystem.dtos;

public class Work {
	private long workTime;
	private int cpu;
	private long cpuPower;
	public long getWorkTime() {
		return workTime;
	}
	public void setWorkTime(long workTime) {
		this.workTime = workTime;
	}
	public int getCpu() {
		return cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	public long getCpuPower() {
		return cpuPower;
	}
	public void setCpuPower(long cpuPower) {
		this.cpuPower = cpuPower;
	}
	
	
}

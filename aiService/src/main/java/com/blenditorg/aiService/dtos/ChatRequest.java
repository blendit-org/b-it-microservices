package com.blenditorg.aiService.dtos;

import java.util.List;

public class ChatRequest {
    private String model;
    private List<Message> messages;
    private double temperature;

    public static class Message {
        private String role;
        private String content;
        // getters/setters
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
        
    }

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public ChatRequest(String model, List<Message> messages, double temperature) {
		super();
		this.model = model;
		this.messages = messages;
		this.temperature = temperature;
	}
    
}

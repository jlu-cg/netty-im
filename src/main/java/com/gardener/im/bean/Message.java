package com.gardener.im.bean;

public class Message {
	private String fromId;
	private String toId;
	private String text;
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}

package com.agkminds.dude.Models;

public class Message {
    private String messageId, senderId, message;
    private long timestamp;

    public Message() {
    }

    public Message(String senderId, String message, long timestamp) {
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}

package com.example.giscord.dto;

import java.time.Instant;

public class ChannelMessageDto {
    private Long messageId;
    private Long channelId;
    private Long senderId;
    private String senderName;
    private String content;
    private Instant createdAt;

    public ChannelMessageDto() {}

    public ChannelMessageDto(Long messageId, Long channelId, Long senderId, String senderName, String content, Instant createdAt) {
        this.messageId = messageId; this.channelId = channelId; this.senderId = senderId;
        this.senderName = senderName; this.content = content; this.createdAt = createdAt;
    }

    // getters / setters...
    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }
    public Long getChannelId() { return channelId; }
    public void setChannelId(Long channelId) { this.channelId = channelId; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}


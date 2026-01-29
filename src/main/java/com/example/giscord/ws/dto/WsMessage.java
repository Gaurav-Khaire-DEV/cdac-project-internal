package com.example.giscord.ws.dto;

// TODO: obtain userId from jwt
public class WsMessage {

    private String type;
    private Long channelId;
    private Object payload;
    private Long userId;

    public WsMessage() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }
}



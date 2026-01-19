
package com.example.giscord.ws.dto;

public class ChannelMessagePayload {

    private String imageUrl;

    private String content;

    public ChannelMessagePayload() {}

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}


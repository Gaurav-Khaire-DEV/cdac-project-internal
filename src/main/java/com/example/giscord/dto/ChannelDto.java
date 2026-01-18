package com.example.giscord.dto;

import java.time.Instant;
import java.util.List;

public class ChannelDto {
    private Long channelId;
    private Long guildId;
    private Long adminUserId;
    private String channelName;
    private Instant createdAt;
    private Instant updatedAt;
    private List<ChannelMemberDto> members;

    public ChannelDto() {}

    public ChannelDto(Long channelId, Long guildId, Long adminUserId, String channelName,
                      Instant createdAt, Instant updatedAt, List<ChannelMemberDto> members) {
        this.channelId = channelId; this.guildId = guildId; this.adminUserId = adminUserId;
        this.channelName = channelName; this.createdAt = createdAt; this.updatedAt = updatedAt;
        this.members = members;
    }

    // getters / setters...
    public Long getChannelId() { return channelId; }
    public void setChannelId(Long channelId) { this.channelId = channelId; }
    public Long getGuildId() { return guildId; }
    public void setGuildId(Long guildId) { this.guildId = guildId; }
    public Long getAdminUserId() { return adminUserId; }
    public void setAdminUserId(Long adminUserId) { this.adminUserId = adminUserId; }
    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<ChannelMemberDto> getMembers() { return members; }
    public void setMembers(List<ChannelMemberDto> members) { this.members = members; }
}


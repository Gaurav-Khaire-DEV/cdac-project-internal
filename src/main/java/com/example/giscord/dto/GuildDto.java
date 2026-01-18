package com.example.giscord.dto;

import java.time.Instant;
import java.util.List;

public class GuildDto {
    private Long guildId;
    private String guildName;
    private Instant createdAt;
    private Instant updatedAt;
    private List<MemberDto> members; // optional, can be omitted or limited

    // constructors, getters, setters
    public GuildDto() {}
    public GuildDto(Long guildId, String guildName, Instant createdAt, Instant updatedAt, List<MemberDto> members) {
        this.guildId = guildId; this.guildName = guildName; this.createdAt = createdAt; this.updatedAt = updatedAt; this.members = members;
    }
    // getters/setters...


    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<MemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDto> members) {
        this.members = members;
    }
}


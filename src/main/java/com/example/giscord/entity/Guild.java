package com.example.giscord.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "guilds")
public class Guild {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guildId;

    @Column(nullable = false)
    private String guildName;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    // One guild has many memberships
    @OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GuildMembership> members = new HashSet<>();

    public Guild() {}

    public Guild(String guildName) {
        this.guildName = guildName;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // convenience helper to add membership
    public void addMember(GuildMembership gm) {
        members.add(gm);
        gm.setGuild(this);
    }

    public void removeMember(GuildMembership gm) {
        members.remove(gm);
        gm.setGuild(null);
    }

    // getters/setters
    public Long getGuildId() { return guildId; }
    public String getGuildName() { return guildName; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Set<GuildMembership> getMembers() { return members; }
    public void setGuildName(String guildName) { this.guildName = guildName; }
}


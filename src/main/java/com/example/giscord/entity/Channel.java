package com.example.giscord.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "channels")
public class Channel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guild_id", nullable = false)
    private Guild guild;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_user_id", nullable = false)
    private User adminUser;

    @Column(nullable = false)
    private String channelName;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChannelMembership> members = new HashSet<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChannelMessage> messages = new HashSet<>();

    public Channel() {}

    public Channel(Guild guild, User adminUser, String channelName) {
        this.guild = guild;
        this.adminUser = adminUser;
        this.channelName = channelName;
    }

    @PreUpdate
    public void onUpdate() { this.updatedAt = Instant.now(); }

    // getters / setters
    public Long getChannelId() { return channelId; }
    public Guild getGuild() { return guild; }
    public User getAdminUser() { return adminUser; }
    public String getChannelName() { return channelName; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Set<ChannelMembership> getMembers() { return members; }
    public Set<ChannelMessage> getMessages() { return messages; }

    public void setGuild(Guild guild) { this.guild = guild; }
    public void setAdminUser(User adminUser) { this.adminUser = adminUser; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public void addMember(ChannelMembership cm) {
        members.add(cm); cm.setChannel(this);
    }
    public void removeMember(ChannelMembership cm) {
        members.remove(cm); cm.setChannel(null);
    }
}



package com.example.giscord.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "channel_messages")
public class ChannelMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Instant createdAt = Instant.now();
    private Instant editedAt;

    public ChannelMessage() {}

    public ChannelMessage(Channel channel, User sender, String content) {
        this.channel = channel; this.sender = sender; this.content = content;
    }

    // getters / setters
    public Long getMessageId() { return messageId; }
    public Channel getChannel() { return channel; }
    public User getSender() { return sender; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getEditedAt() { return editedAt; }

    public void setChannel(Channel channel) { this.channel = channel; }
    public void setSender(User sender) { this.sender = sender; }
    public void setContent(String content) { this.content = content; }
    public void setEditedAt(Instant editedAt) { this.editedAt = editedAt; }
}


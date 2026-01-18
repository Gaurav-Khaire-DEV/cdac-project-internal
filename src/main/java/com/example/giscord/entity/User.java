package com.example.giscord.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    @JsonIgnore
    private String passwordHash;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    // bidirectional convenience (not required right now)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GuildMembership> guildMemberships = new HashSet<>();

    public User() {}

    public User(String userName, String passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // getters/setters
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getPasswordHash() { return passwordHash; } // keep but it's @JsonIgnore
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Set<GuildMembership> getGuildMemberships() { return guildMemberships; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}

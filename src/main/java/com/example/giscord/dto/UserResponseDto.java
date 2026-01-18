package com.example.giscord.dto;

import java.time.Instant;

public class UserResponseDto {
    private Long userId;
    private String userName;
    private Instant createdAt;
    private Instant updatedAt;

    public UserResponseDto() {}

    public UserResponseDto(Long userId, String userName, Instant createdAt, Instant updatedAt) {
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}


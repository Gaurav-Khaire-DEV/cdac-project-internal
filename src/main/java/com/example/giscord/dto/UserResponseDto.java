package com.example.giscord.dto;

import java.time.Instant;

public record UserResponseDto (
    Long userId,
    String userName,
    Instant createdAt,
    Instant updatedAt
) {}

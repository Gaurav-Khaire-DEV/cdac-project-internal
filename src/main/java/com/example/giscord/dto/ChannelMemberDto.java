package com.example.giscord.dto;

import java.time.Instant;

public record ChannelMemberDto(
    Long userId,
    String userName,
    String role,
    Instant joinedAt
) {}


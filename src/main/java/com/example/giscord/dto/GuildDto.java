package com.example.giscord.dto;

import java.time.Instant;
import java.util.List;

public record GuildDto(
        Long guildId,
        String guildName,
        Instant createdAt,
        Instant updatedAt,
        List<MemberDto> members
){}

package com.example.giscord.dto;

import java.time.Instant;
import java.util.List;

public record ChannelDto (
        Long channelId,
        Long guildId,
        Long adminUserId,
        String channelName,
        Instant createdAt,
        Instant updatedAt,
        List<MemberDto> members
) {}

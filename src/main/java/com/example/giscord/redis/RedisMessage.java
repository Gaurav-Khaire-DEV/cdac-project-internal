package com.example.giscord.redis;

import java.time.Instant;
import java.util.List;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
public record RedisMessage (
    Long channelId,
    Long userId,
    String content,
    List<Long> attachmentIds,
    Instant createdAt
){}

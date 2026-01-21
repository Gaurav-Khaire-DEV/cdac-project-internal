package com.example.giscord.service;

import com.example.giscord.entity.Message;
import com.example.giscord.redis.RedisMessage;
import com.example.giscord.repository.MessageRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageFlushService {

    private final RedisTemplate<String, RedisMessage> redisTemplate;
    private final MessageRepository messageRepository;

    public MessageFlushService(
            RedisTemplate<String, RedisMessage> redisTemplate,
            MessageRepository messageRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.messageRepository = messageRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void flush() {

        // example: iterate known channels (store channel IDs elsewhere)
        // simplified example assumes channel:3
        List<RedisMessage> batch =
                redisTemplate.opsForList().range("channel:3", 0, -1);

        if (batch == null || batch.isEmpty()) return;

        redisTemplate.delete("channel:3");

        for (RedisMessage rm : batch) {
            Message m = new Message();
            m.setChannelId(rm.channelId());
            m.setSenderUserId(rm.userId());
            m.setContent(rm.content());
            m.setCreatedAt(rm.createdAt());

            messageRepository.save(m);
        }
    }
}

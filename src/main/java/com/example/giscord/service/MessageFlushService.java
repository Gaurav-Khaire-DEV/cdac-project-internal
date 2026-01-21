package com.example.giscord.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.giscord.entity.Message;
import com.example.giscord.redis.RedisMessage;
import com.example.giscord.repository.MessageRepository;

@Service
public class MessageFlushService {

    private final RedisTemplate<String, RedisMessage> redisMessageTemplate;
    private final RedisTemplate<String, String> redisStringTemplate;
    private final MessageRepository messageRepository;

    public MessageFlushService(
            RedisTemplate<String, RedisMessage> redisMessageTemplate,
            RedisTemplate<String, String> redisStringTemplate,
            MessageRepository messageRepository
    ) {
        this.redisMessageTemplate = redisMessageTemplate;
        this.redisStringTemplate = redisStringTemplate;
        this.messageRepository = messageRepository;
    }


    @Scheduled(fixedDelay = 5000)
    public void flush() {
        
        Set<String> channelIds = redisStringTemplate.opsForSet().members("active:channels");

        if (channelIds == null || channelIds.isEmpty()) {
            return;
        }

        for (String channelId: channelIds) {
            String key = "channel:" + channelId;

            List<RedisMessage> batch = redisMessageTemplate.opsForList().range(key, 0, -1);

            if (batch == null || batch.isEmpty()) {
                redisStringTemplate.opsForSet().remove("active:channels", channelId);
                continue;
            }
            
            redisMessageTemplate.delete(key);
            redisStringTemplate.opsForSet().remove("active:channels", channelId);

            for (RedisMessage rm: batch) {

                Message m = new Message();
                m.setChannelId(rm.channelId());
                m.setSenderUserId(rm.userId());
                m.setContent(rm.content());
                m.setCreatedAt(rm.createdAt());


                messageRepository.save(m);

            }
            

        }
    }

    /*
    @Scheduled(fixedDelay = 5000)
    public void flush() {

        // System.out.println("Flushing messages from Redis to DB at " + Instant.now().toString());

        // example: iterate known channels (store channel IDs elsewhere)
        // simplified example assumes channel:3
        // TODO: improve with SCAN or other method to avoid unsafe casts
        // @SuppressWarnings("unchecked")
        // List<RedisMessage> batch =
        //         (List<RedisMessage>) (List<?>)
        //         redisTemplate.opsForList().range("channel:2", 0, -1);
        
        List<RedisMessage> batch = redisTemplate.opsForSet().add("active:channels", String.valueOf(channelId))

        if (batch == null || batch.isEmpty()) return;

        redisTemplate.delete("channel:2");

        for (RedisMessage rm : batch) {
            Message m = new Message();
            m.setChannelId(rm.channelId());
            m.setSenderUserId(rm.userId());
            m.setContent(rm.content());
            m.setCreatedAt(rm.createdAt());

            messageRepository.save(m);
        }
    }
    */
}

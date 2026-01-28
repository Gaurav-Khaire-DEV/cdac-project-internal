package com.example.giscord.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.giscord.entity.Attachment;
import com.example.giscord.entity.Message;
import com.example.giscord.redis.RedisMessage;
import com.example.giscord.repository.AttachmentRepository;
import com.example.giscord.repository.MessageRepository;

@Service
public class MessageFlushService {

    private final RedisTemplate<String, RedisMessage> redisMessageTemplate;
    private final RedisTemplate<String, String> redisStringTemplate;
    private final MessageRepository messageRepository;
    private final AttachmentRepository attachmentRepository;

    public MessageFlushService(
            RedisTemplate<String, RedisMessage> redisMessageTemplate,
            RedisTemplate<String, String> redisStringTemplate,
            MessageRepository messageRepository,
            AttachmentRepository attachmentRepository
    ) {
        this.redisMessageTemplate = redisMessageTemplate;
        this.redisStringTemplate = redisStringTemplate;
        this.messageRepository = messageRepository;
        this.attachmentRepository = attachmentRepository;
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
                
                if (rm.attachmentIds() != null && !rm.attachmentIds().isEmpty()) {
                    List<Attachment> attachments = attachmentRepository.findAllById(rm.attachmentIds());
                    attachments.forEach(m::addAttachment);
                }
                messageRepository.save(m);
            }
        }
    }
}

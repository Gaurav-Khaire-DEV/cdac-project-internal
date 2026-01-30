package com.example.giscord.controller;

import java.util.List;

import com.example.giscord.entity.Attachment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.giscord.dto.MessageDto;
import com.example.giscord.entity.Message;
import com.example.giscord.repository.MessageRepository;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/{channelId}")
    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(@PathVariable Long channelId) {
        // TODO: MessageService -> toDto(Message) => MessageDto
        List<Message> messages = messageRepository.findTop50ByChannelIdOrderByCreatedAtDesc(channelId);
         return messages.stream().map(m -> new MessageDto(
                m.getId(),
                m.getChannelId(),
                m.getSenderUserId(),
                m.getContent(),
                m.getCreatedAt(),
                m.getAttachments() != null
                        ? m.getAttachments().stream().map(Attachment::getId).toList()
                        : null
        )).toList();

    }
    
}

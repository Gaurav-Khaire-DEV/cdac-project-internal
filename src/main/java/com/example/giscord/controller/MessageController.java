package com.example.giscord.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Message> getMessages(@PathVariable Long channelId) {
        // TODO: MessageService -> toDto(Message) => MessageDto
        return messageRepository.findTop50ByChannelIdOrderByCreatedAtDesc(channelId);
    }
    
}

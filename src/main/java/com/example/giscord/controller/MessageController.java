package com.example.giscord.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.giscord.entity.Message;
import com.example.giscord.repository.MessageRepository;

@RestController
@RequestMapping("/api/channels")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    // TODO: MessageService ??
    // TODO: MessageDto ??
    @GetMapping("/{channelId}/messages")
    public List<Message> getMessages(@PathVariable Long channelId) {
        return messageRepository.findTop50ByChannelIdOrderByCreatedAtDesc(channelId);
    }
    
}

package com.example.giscord.controller;

import com.example.giscord.dto.ChannelDto;
import com.example.giscord.entity.Channel;
import com.example.giscord.repository.MessageRepository;
import com.example.giscord.security.CustomUserDetails;
import com.example.giscord.service.ChannelService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    public ChannelController(ChannelService channelService, MessageRepository messageRepository) {
        this.channelService = channelService;
        this.messageRepository = messageRepository;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        Long guildId = Long.valueOf(body.get("guildId"));
        Long adminUserId = Long.valueOf(body.get("adminUserId"));
        String channelName = body.get("name");
        try {
            Channel c = channelService.createChannel(guildId, adminUserId, channelName);
            ChannelDto dto = channelService.toDto(c, 10);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
   }

    @PostMapping(path = "/{channelId}/join", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> join(@PathVariable Long channelId, @RequestBody Map<String, String> body) {
        String role = body.get("role");
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomUserDetails cud)) {
            return ResponseEntity.status(401).body(Map.of("error", "not authenticated"));
        }
        Long userId = cud.getUserId();
        try {
            Channel c = channelService.joinChannel(channelId, userId, role);
            ChannelDto dto = channelService.toDto(c, 50);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}


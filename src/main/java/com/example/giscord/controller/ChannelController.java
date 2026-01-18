package com.example.giscord.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.giscord.dto.ChannelDto;
import com.example.giscord.dto.ChannelMessageDto;
import com.example.giscord.entity.Channel;
import com.example.giscord.entity.ChannelMessage;
import com.example.giscord.security.CustomUserDetails;
import com.example.giscord.service.ChannelService;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) { this.channelService = channelService; }

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

    /*
    @PostMapping(path = "/{channelId}/join", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> join(@PathVariable Long channelId, @RequestBody Map<String, String> body) {
        Long userId = Long.valueOf(body.get("userId"));
        String role = body.get("role");
        try {
            Channel c = channelService.joinChannel(channelId, userId, role);
            ChannelDto dto = channelService.toDto(c, 50);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
     */

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


    @PostMapping(path = "/{channelId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMessage(@PathVariable Long channelId, @RequestBody Map<String, String> body) {
        Long senderId = Long.valueOf(body.get("senderId"));
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "content required"));
        }
        ChannelMessage m = channelService.sendMessage(channelId, senderId, content);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                new ChannelMessageDto(m.getMessageId(), channelId, m.getSender().getUserId(), m.getSender().getUserName(), m.getContent(), m.getCreatedAt())
        );
    }

    @GetMapping(path = "/{channelId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChannelMessageDto>> listMessages(@PathVariable Long channelId,
                                                                @RequestParam(name = "limit", required = false, defaultValue = "50") int limit) {
        List<ChannelMessageDto> msgs = channelService.listMessages(channelId, limit);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(msgs);
    }
}


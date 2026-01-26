
package com.example.giscord.controller;

import com.example.giscord.dto.GuildCreationRequestDto;
import com.example.giscord.dto.GuildDto;
import com.example.giscord.entity.Guild;
import com.example.giscord.service.GuildService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/guilds")
public class GuildController {
    private final GuildService guildService;

    public GuildController(GuildService guildService) { this.guildService = guildService; }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuildDto> create(@RequestBody GuildCreationRequestDto req) {
        Guild guild = guildService.createGuild(req.guildName(), req.ownerId(), req.description());
        GuildDto dto = guildService.toDto(guild, 10);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(path = "/{guildId}/join", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> join(@PathVariable Long guildId, @RequestBody Map<String, String> body) {
        Long userId = Long.valueOf(body.get("userId"));
        String role = body.get("role"); // optional
        try {
            // TODO: Write Decent Code dude ...
            Guild g = guildService.joinGuild(guildId, userId, role) == true ? guildService.findById(guildId).get(): null;
            GuildDto gto = guildService.toDto(g, 10);
            return ResponseEntity.ok(gto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuildDto> get(@PathVariable Long id) {
        return guildService.findById(id)
                .map(g -> ResponseEntity.ok(guildService.toDto(g, 10)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


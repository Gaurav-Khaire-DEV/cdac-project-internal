package com.example.giscord.service;

import com.example.giscord.entity.User;
import com.example.giscord.entity.Guild;
import com.example.giscord.entity.GuildMembership;
import com.example.giscord.entity.GuildMembershipId;

import com.example.giscord.dto.GuildDto;
import com.example.giscord.dto.MemberDto;

import com.example.giscord.repository.GuildMembershipRepository;
import com.example.giscord.repository.GuildRepository;
import com.example.giscord.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GuildService {
    private final GuildRepository guildRepo;
    private final UserRepository userRepo;
    private final GuildMembershipRepository membershipRepo;

    public GuildService(GuildRepository guildRepo, UserRepository userRepo, GuildMembershipRepository membershipRepo) {
        this.guildRepo = guildRepo;
        this.userRepo = userRepo;
        this.membershipRepo = membershipRepo;
    }

    public Guild createGuild(String name) {
        Guild g = new Guild(name);
        return guildRepo.save(g);
    }

    @Transactional
    public Guild joinGuild(Long guildId, Long userId, String role) {

        Guild guild = guildRepo.findById(guildId).orElseThrow(() -> new IllegalArgumentException("guild not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));

        // if membership exists, return guild
        GuildMembershipId gid = new GuildMembershipId(guildId, userId);
        if (membershipRepo.existsById(gid)) return guild;

        GuildMembership gm = new GuildMembership();
        gm.setGuild(guild);
        gm.setUser(user);
        gm.setRole(role == null ? "member" : role);

        // set embedded id explicitly to avoid surprises
        gm.getId().setGuildId(guildId);
        gm.getId().setUserId(userId);

        membershipRepo.save(gm);

        // refresh and return
        // JUGAAD: should be updated
        // TODO: Member limit is 100 for no reason
        return guildRepo.findById(guildId).orElseThrow();
    }

    public Optional<Guild> findById(Long id) {
        return guildRepo.findById(id);
    }

    public GuildDto toDto(Guild guild, int memberLimit) {
        List<MemberDto> members = guild.getMembers().stream()
                .limit(memberLimit > 0 ? memberLimit : Long.MAX_VALUE)
                .map(gm -> new MemberDto(
                        gm.getUser().getUserId(),
                        gm.getUser().getUserName(),
                        gm.getRole(),
                        gm.getJoinedAt()
                ))
                .collect(Collectors.toList());

        return new GuildDto(
                guild.getGuildId(),
                guild.getGuildName(),
                guild.getCreatedAt(),
                guild.getUpdatedAt(),
                members
        );
    }
}


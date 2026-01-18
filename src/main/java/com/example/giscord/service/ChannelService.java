package com.example.giscord.service;

import com.example.giscord.dto.ChannelDto;
import com.example.giscord.dto.ChannelMemberDto;
import com.example.giscord.dto.ChannelMessageDto;
import com.example.giscord.entity.*;
import com.example.giscord.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    private final ChannelRepository channelRepo;
    private final GuildRepository guildRepo;
    private final UserRepository userRepo;
    private final ChannelMembershipRepository membershipRepo;
    private final ChannelMessageRepository messageRepo;

    public ChannelService(ChannelRepository channelRepo,
                          GuildRepository guildRepo,
                          UserRepository userRepo,
                          ChannelMembershipRepository membershipRepo,
                          ChannelMessageRepository messageRepo) {
        this.channelRepo = channelRepo;
        this.guildRepo = guildRepo;
        this.userRepo = userRepo;
        this.membershipRepo = membershipRepo;
        this.messageRepo = messageRepo;
    }

    @Transactional
    public Channel createChannel(Long guildId, Long adminUserId, String channelName) {
        Guild guild = guildRepo.findById(guildId).orElseThrow(() -> new IllegalArgumentException("guild not found"));
        User admin = userRepo.findById(adminUserId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        Channel c = new Channel(guild, admin, channelName);
        Channel saved = channelRepo.save(c);

        // create admin membership
        ChannelMembership cm = new ChannelMembership();
        cm.setChannel(saved);
        cm.setUser(admin);
        cm.setRole("owner");
        cm.getId().setChannelId(saved.getChannelId());
        cm.getId().setUserId(admin.getUserId());
        membershipRepo.save(cm);

        return saved;
    }

    @Transactional
    public Channel joinChannel(Long channelId, Long userId, String role) {
        Channel channel = channelRepo.findById(channelId).orElseThrow(() -> new IllegalArgumentException("channel not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));

        ChannelMembershipId id = new ChannelMembershipId(channelId, userId);
        if (membershipRepo.existsById(id)) return channel;

        ChannelMembership cm = new ChannelMembership();
        cm.setChannel(channel);
        cm.setUser(user);
        cm.setRole(role == null ? "member" : role);
        cm.getId().setChannelId(channelId);
        cm.getId().setUserId(userId);
        membershipRepo.save(cm);

        return channel;
    }

    @Transactional
    public ChannelMessage sendMessage(Long channelId, Long senderId, String content) {
        Channel channel = channelRepo.findById(channelId).orElseThrow(() -> new IllegalArgumentException("channel not found"));
        User sender = userRepo.findById(senderId).orElseThrow(() -> new IllegalArgumentException("user not found"));

        ChannelMessage m = new ChannelMessage(channel, sender, content);
        return messageRepo.save(m);
    }

    @Transactional(readOnly = true)
    public Optional<Channel> findByIdWithMembers(Long id) {
        return channelRepo.findByIdWithMembersAndUsers(id);
    }

    @Transactional(readOnly = true)
    public Optional<Channel> findByIdWithMessages(Long id) {
        return channelRepo.findByIdWithMessagesAndSenders(id);
    }

    @Transactional(readOnly = true)
    public ChannelDto toDto(Channel c, int memberLimit) {
        List<ChannelMemberDto> members = c.getMembers().stream()
                .limit(memberLimit > 0 ? memberLimit : Integer.MAX_VALUE)
                .map(cm -> new ChannelMemberDto(
                        cm.getUser().getUserId(),
                        cm.getUser().getUserName(),
                        cm.getRole(),
                        cm.getJoinedAt()
                ))
                .collect(Collectors.toList());

        return new ChannelDto(
                c.getChannelId(),
                c.getGuild().getGuildId(),
                c.getAdminUser().getUserId(),
                c.getChannelName(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                members
        );
    }

    @Transactional(readOnly = true)
    public List<ChannelMessageDto> listMessages(Long channelId, int limit) {
        var page = PageRequest.of(0, Math.max(1, limit));
        List<ChannelMessage> msgs = messageRepo.findByChannelIdOrderByCreatedAtDesc(channelId, page);
        return msgs.stream()
                .map(m -> new ChannelMessageDto(
                        m.getMessageId(),
                        m.getChannel().getChannelId(),
                        m.getSender().getUserId(),
                        m.getSender().getUserName(),
                        m.getContent(),
                        m.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}


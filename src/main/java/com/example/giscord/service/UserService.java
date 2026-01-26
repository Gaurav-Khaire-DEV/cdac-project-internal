package com.example.giscord.service;

import com.example.giscord.dto.UserResponseDto;
import com.example.giscord.entity.User;
import com.example.giscord.repository.ChannelMembershipRepository;
import com.example.giscord.repository.GuildMembershipRepository;
import com.example.giscord.repository.UserRepository;
import com.example.giscord.repository.projection.ChannelIdNameView;
import com.example.giscord.repository.projection.GuildIdNameView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ChannelMembershipRepository channelMembershipRepository;
    private final GuildMembershipRepository guildMembershipRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder,
                       ChannelMembershipRepository channelMembershipRepository, GuildMembershipRepository guildMembershipRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.channelMembershipRepository = channelMembershipRepository;
        this.guildMembershipRepository = guildMembershipRepository;
    }

    public User registerUser(String userName, String plainPassword, String description) {
        String hashed = encoder.encode(plainPassword);
        User user = new User(userName, hashed);
        user.setDescription(description); // TODO: why not modify the ctor ??
        return userRepository.save(user);
    }

    public boolean verifyPassword(String userName, String plainPassword) {
        return userRepository.findByUserName(userName)
                .map(user -> encoder.matches(plainPassword, user.getPasswordHash()))
                .orElse(false);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
            user.getUserId(),
            user.getUserName(),
            user.getDescription(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getProfileAttachment() != null
                ? user.getProfileAttachment().getId()
                : null
        );
    }

    public List<ChannelIdNameView> getAllChannelIdsAndNamesByUserId(Long userId) {
        return channelMembershipRepository.findChannelIdAndNameFromUserId(userId);
    }

    public List<GuildIdNameView> getAllGuildIdsAndNamesByUserId(Long userId) {
        return guildMembershipRepository.findGuildIdAndNameFromUserId(userId);
    }
}

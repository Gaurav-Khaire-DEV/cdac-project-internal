package com.example.giscord.repository;

import com.example.giscord.entity.ChannelMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChannelMessageRepository extends JpaRepository<ChannelMessage, Long> {

    @Query("select m from ChannelMessage m where m.channel.channelId = :channelId order by m.createdAt desc")
    List<ChannelMessage> findByChannelIdOrderByCreatedAtDesc(Long channelId, Pageable pageable);
}


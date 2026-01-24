package com.example.giscord.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.giscord.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
    // How the hell does this work ??
    List<Message> findTop50ByChannelIdOrderByCreatedAtDesc(Long channelId);

    List<Message> findAllByChannelIdOrderByCreatedAtDesc(Long channelId);
}

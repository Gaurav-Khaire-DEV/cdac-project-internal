package com.example.giscord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.giscord.entity.ChannelMembership;
import com.example.giscord.entity.ChannelMembershipId;

@Repository
public interface ChannelMembershipRepository extends JpaRepository<ChannelMembership, ChannelMembershipId> {
    @Query("""
        select count(cm) > 0
        from ChannelMembership cm
        where cm.channel.id = :channelId
          and cm.user.id = :userId
    """)
    boolean existsByChannelAndUser(
            @Param("channelId") Long channelId,
            @Param("userId") Long userId
    );
}

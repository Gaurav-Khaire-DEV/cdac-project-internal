package com.example.giscord.repository;

import com.example.giscord.entity.GuildMembership;
import com.example.giscord.entity.GuildMembershipId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuildMembershipRepository extends JpaRepository<GuildMembership, GuildMembershipId> {
    @Query("""
       select gm.id.guildId from
       GuildMembership gm
       where gm.id.userId = :userId
    """)
    public List<Long> findGuildIdByUserId(@Param("userId") Long userId);
}

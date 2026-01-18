package com.example.giscord.repository;

import com.example.giscord.entity.GuildMembership;
import com.example.giscord.entity.GuildMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuildMembershipRepository extends JpaRepository<GuildMembership, GuildMembershipId> {
}

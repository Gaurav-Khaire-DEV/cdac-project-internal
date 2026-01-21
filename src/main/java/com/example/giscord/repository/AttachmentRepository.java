package com.example.giscord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.giscord.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
}

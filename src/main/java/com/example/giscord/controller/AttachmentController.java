package com.example.giscord.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.giscord.entity.Attachment;
import com.example.giscord.repository.AttachmentRepository;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    private final AttachmentRepository attachmentRepository;
    private final S3Client s3Client;

    private final String bucket;

    public AttachmentController(AttachmentRepository attachmentRepository, S3Client s3Client, @Value("${minio.bucket}") String bucket) {
        this.attachmentRepository = attachmentRepository;
        this.s3Client = s3Client;
        this.bucket = bucket; // is this right way to do this ?? what are other ways ??
    }

    @PostMapping
    public ResponseEntity<?> uploadAttachment(@RequestParam("file") MultipartFile file) throws Exception {

        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build(),
            software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        Attachment a = new Attachment();
        a.setBucket(bucket);
        a.setContentType(file.getContentType());
        a.setObjectKey(key);
        a.setSize(file.getSize());


        attachmentRepository.save(a);
        
        return ResponseEntity
            .status(201)
            .body(Map.of("attachmendId", a.getId()));
    }

    
}

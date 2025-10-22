package io.github.mgluizbrito.mediaconduit_api.controller;

import io.github.mgluizbrito.mediaconduit_api.service.DownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/downloads")
@RequiredArgsConstructor
public class DownloadsController {

    private final DownloadService downloadService;

    @GetMapping("/{jobId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID jobId) {
        var task = downloadService.getTaskStatus(jobId);
        
        if (task.getStatus() != io.github.mgluizbrito.mediaconduit_api.model.JobStatus.COMPLETED) {
            return ResponseEntity.badRequest().build();
        }
        
        if (task.getFileUrl() == null) {
            return ResponseEntity.notFound().build();
        }
        
        File file = new File(task.getFileUrl());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        Resource resource = new FileSystemResource(file);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }
}
package io.github.mgluizbrito.mediaconduit_api.service;

import io.github.mgluizbrito.mediaconduit_api.cli.CommandExecutor;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractRequestDTO;
import io.github.mgluizbrito.mediaconduit_api.exceptions.MediaExtractionException;
import io.github.mgluizbrito.mediaconduit_api.model.ExtractionTask;
import io.github.mgluizbrito.mediaconduit_api.model.ExtractorsAvailable;
import io.github.mgluizbrito.mediaconduit_api.model.JobStatus;
import io.github.mgluizbrito.mediaconduit_api.repository.ExtractionTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DownloadService {

    private final ExtractionTaskRepository taskRepository;
    private final FileSystemService fileSystemService;

    public ExtractionTask createDownloadTask(ExtractRequestDTO request, ExtractorsAvailable extractor) {
        UUID jobId = UUID.randomUUID();
        
        // Garantir que o diretório de download existe
        fileSystemService.ensureDownloadDirectoryExists();
        
        ExtractionTask task = new ExtractionTask();
        task.setJobId(jobId);
        task.setMediaIdentifier(request.mediaIdentifier());
        task.setExtractor(extractor);
        task.setStatus(JobStatus.PENDING);
        task.setProgress(0);
        task.setStartTime(LocalDateTime.now());

        // Salvar a task no banco
        task = taskRepository.save(task);
        
        log.info("Task de download criada com jobId: {} para media: {}", jobId, request.mediaIdentifier());
        
        return task;
    }

    public ExtractionTask getTaskStatus(UUID jobId) {
        return taskRepository.findByJobId(jobId)
                .orElseThrow(() -> new MediaExtractionException("Task não encontrada com jobId: " + jobId, "VERIFIQUE O JobId"));
    }

    public void updateTaskProgress(UUID jobId, int progress) {
        taskRepository.findByJobId(jobId).ifPresent(task -> {
            task.setProgress(progress);
            taskRepository.save(task);
        });
    }

    public void updateTaskStatus(UUID jobId, JobStatus status) {
        taskRepository.findByJobId(jobId).ifPresent(task -> {
            task.setStatus(status);
            if (status == JobStatus.COMPLETED || status == JobStatus.FAILED) {
                task.setFinishTime(LocalDateTime.now());
            }
            taskRepository.save(task);
        });
    }

    public void updateTaskFileUrl(UUID jobId, String fileUrl) {
        taskRepository.findByJobId(jobId).ifPresent(task -> {
            task.setFileUrl(fileUrl);
            taskRepository.save(task);
        });
    }

    public void updateTaskError(UUID jobId, String errorMessage) {
        taskRepository.findByJobId(jobId).ifPresent(task -> {
            task.setErrorMessage(errorMessage);
            task.setStatus(JobStatus.FAILED);
            task.setFinishTime(LocalDateTime.now());
            taskRepository.save(task);
        });
    }
}

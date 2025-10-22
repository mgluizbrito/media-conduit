package io.github.mgluizbrito.mediaconduit_api.controller.dto;

import io.github.mgluizbrito.mediaconduit_api.model.JobStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskStatusDTO(
        UUID jobId,
        String mediaIdentifier,
        JobStatus status,
        int progress,
        LocalDateTime startTime,
        LocalDateTime finishTime,
        String fileUrl,
        String errorMessage
) {
}

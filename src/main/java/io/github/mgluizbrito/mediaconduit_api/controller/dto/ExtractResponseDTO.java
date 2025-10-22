package io.github.mgluizbrito.mediaconduit_api.controller.dto;

import java.util.UUID;

public record ExtractResponseDTO(
        UUID jobId,
        String status,
        String trackingURL
) {
}

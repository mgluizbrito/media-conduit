package io.github.mgluizbrito.mediaconduit_api.controller.dto;

import java.net.URL;

public record ExtractResponseDTO(
        String jobId,
        String status,
        URL trackingURL
    ) {
}

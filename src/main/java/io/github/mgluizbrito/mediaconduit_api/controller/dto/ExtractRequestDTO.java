package io.github.mgluizbrito.mediaconduit_api.controller.dto;

import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractConfigs.ExtractConfigDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExtractRequestDTO(
        @NotBlank(message = "ID or URL of the media cannot be empty.")
        String mediaIdentifier,
        @NotNull(message = "The file format is required.")
        FileFormats format,
        String notificationEmail,
        ExtractConfigDTO config
) {
}

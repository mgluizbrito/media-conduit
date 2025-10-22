package io.github.mgluizbrito.mediaconduit_api.controller.dto;

public record FormatInfoDTO(
        String formatId,
        String ext,
        String resolution,
        String vcodec,
        String acodec,
        Double tbr,
        String formatUrl,
        Integer height,
        Integer width
) {
}

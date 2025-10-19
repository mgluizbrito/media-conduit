package io.github.mgluizbrito.mediaconduit_api.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileFormats {

    MP4("video/mp4"),
    MOV("video/quicktime"),
    WEBM("video/webm"),
    MP3("audio/mpeg"),
    WAV("audio/wav");

    private final String mimeType;
}

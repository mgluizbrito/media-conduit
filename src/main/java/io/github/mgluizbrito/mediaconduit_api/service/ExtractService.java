package io.github.mgluizbrito.mediaconduit_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.mgluizbrito.mediaconduit_api.cli.YtDlpProcessBuilder;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractRequestDTO;
import io.github.mgluizbrito.mediaconduit_api.model.MediaMetadata;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.mgluizbrito.mediaconduit_api.cli.YtDlpProcessBuilder.buildYtdlpDownloadCommand;

@Service
public class ExtractService {

    public MediaMetadata getMetadata(String mediaIdentifier) throws JsonProcessingException {

        List<String> command = List.of(
                "yt-dlp",
                "--dump-json",
                "--skip-download",
                "--add-header", "User-Agent: MediaConduit-API-Client",
                mediaIdentifier
        );

        String jsonOutput = YtDlpProcessBuilder.exeYtDlpCommand(command);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.readValue(jsonOutput, MediaMetadata.class);
    }

}

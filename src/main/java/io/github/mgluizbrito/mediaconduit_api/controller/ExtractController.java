package io.github.mgluizbrito.mediaconduit_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.mgluizbrito.mediaconduit_api.cli.CommandExecutor;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractRequestDTO;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractResponseDTO;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.MediaMetadataDTO;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.TaskStatusDTO;
import io.github.mgluizbrito.mediaconduit_api.controller.mappers.MediaMetadataMapper;
import io.github.mgluizbrito.mediaconduit_api.exceptions.InvalidFieldException;
import io.github.mgluizbrito.mediaconduit_api.model.ExtractorsAvailable;
import io.github.mgluizbrito.mediaconduit_api.model.ExtractionTask;
import io.github.mgluizbrito.mediaconduit_api.model.MediaMetadata;
import io.github.mgluizbrito.mediaconduit_api.service.DownloadService;
import io.github.mgluizbrito.mediaconduit_api.service.ExtractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/extract")
@RequiredArgsConstructor
public class ExtractController {

    private final ExtractService service;
    private final DownloadService downloadService;
    private final MediaMetadataMapper mapper;
    private final CommandExecutor commandExecutor;

    @PostMapping("/{service}")
    public ResponseEntity<ExtractResponseDTO> startExtract(
            @PathVariable("service") String serviceName,
            @RequestBody @Valid ExtractRequestDTO dto, UriComponentsBuilder ucb) {
        
        if (!Arrays.stream(ExtractorsAvailable.values()).anyMatch((e) -> e.name().equalsIgnoreCase(serviceName)))
            throw new InvalidFieldException("[SERVICE ON URL]", "The " + serviceName + " service is not yet available");
        
        ExtractorsAvailable extractor = ExtractorsAvailable.valueOf(serviceName.toUpperCase());
        ExtractionTask task = downloadService.createDownloadTask(dto, extractor);
        commandExecutor.submitDownloadTask(task, dto);
        
        String trackingUrl = ucb.path("/extract/status/{jobId}")
                .buildAndExpand(task.getJobId())
                .toUriString();
        
        return ResponseEntity.ok(new ExtractResponseDTO(
                task.getJobId(), 
                task.getStatus().name(), 
                trackingUrl
        ));
    }

    @GetMapping("/metadata")
    public ResponseEntity<MediaMetadataDTO> metadata(@RequestParam("media") String mediaIdentifier) {

        try {
            MediaMetadata metadata = service.getMetadata(mediaIdentifier);
            return ResponseEntity.ok(mapper.toDto(metadata));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("ERRO: " + e.getMessage());
        }
    }

    @GetMapping("/status/{jobId}")
    public ResponseEntity<TaskStatusDTO> getTaskStatus(@PathVariable UUID jobId) {
        ExtractionTask task = downloadService.getTaskStatus(jobId);
        
        TaskStatusDTO statusDTO = new TaskStatusDTO(
                task.getJobId(),
                task.getMediaIdentifier(),
                task.getStatus(),
                task.getProgress(),
                task.getStartTime(),
                task.getFinishTime(),
                task.getFileUrl(),
                task.getErrorMessage()
        );
        
        return ResponseEntity.ok(statusDTO);
    }
}

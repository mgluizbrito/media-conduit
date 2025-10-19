package io.github.mgluizbrito.mediaconduit_api.controller;

import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractRequestDTO;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/extract/youtube")
public class YoutubeController {

    @PostMapping
    public ResponseEntity<ExtractResponseDTO> startExtract (@RequestBody @Valid ExtractRequestDTO dto, UriComponentsBuilder ucb) throws URISyntaxException, MalformedURLException {
        return ResponseEntity.ok(new ExtractResponseDTO("aa", "bb", new URI("https://www.google.com").toURL()));
    }
}

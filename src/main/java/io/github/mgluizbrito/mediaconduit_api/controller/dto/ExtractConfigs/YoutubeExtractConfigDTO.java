package io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractConfigs;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class YoutubeExtractConfigDTO extends ExtractConfigDTO {
    private boolean includeSubtitles;
    @Positive(message = "The playlist index must be positive")
    private int playlistIndex;
}

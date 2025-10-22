package io.github.mgluizbrito.mediaconduit_api.controller.dto;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

public record MediaMetadataDTO(
        String videoSourceId,
        String title,
        Long duration,
        LocalDate upload,
        String channelUrl,
        BigInteger viewCount,
        BigInteger likeCount,
        String description,
        List<FormatInfoDTO> formats
) {
}

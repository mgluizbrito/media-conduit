package io.github.mgluizbrito.mediaconduit_api.controller.mappers;

import io.github.mgluizbrito.mediaconduit_api.controller.dto.FormatInfoDTO;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.MediaMetadataDTO;
import io.github.mgluizbrito.mediaconduit_api.model.FormatInfo;
import io.github.mgluizbrito.mediaconduit_api.model.MediaMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class MediaMetadataMapper {

    @Mapping(target = "task", ignore = true)
    public abstract MediaMetadata toEntity(MediaMetadataDTO dto);

    public abstract MediaMetadataDTO toDto(MediaMetadata entity);

    /**
     * Helper method that converts Entity FormatInfo to DTO FormatInfoDTO.
     * MapStruct infers this method and applies it to the 'formats' list.
     * <p>
     * Note: MapStruct maps by name. How do field names
     * in FormatInfo and FormatInfoDTO are identical (except for the DTO suffix),
     * no @Mapping annotations are needed unless there are fields with different names.
     */
    public abstract FormatInfoDTO toDto(FormatInfo entity);
}

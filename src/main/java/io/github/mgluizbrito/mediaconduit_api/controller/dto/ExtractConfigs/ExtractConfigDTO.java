package io.github.mgluizbrito.mediaconduit_api.controller.dto.ExtractConfigs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Size;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Tipo de identificação: Nome da subclasse ou valor de propriedade
        include = JsonTypeInfo.As.PROPERTY, // Onde o tipo é incluído: Como uma propriedade no JSON
        property = "dataType" // Nome do campo discriminatório no JSON
)
// Usamos @JsonSubTypes para mapear o valor do campo 'dataType' para a classe Java.
@JsonSubTypes({
        @JsonSubTypes.Type(value = YoutubeExtractConfigDTO.class, name = "YOUTUBE_CONFIG")
})
public abstract class ExtractConfigDTO {
    @Size(min = 1, max = 5, message = "Quality ID must be between 1 and 5 characters long")
    private String qualityId;
}

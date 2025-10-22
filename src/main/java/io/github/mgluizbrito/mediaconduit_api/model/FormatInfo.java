package io.github.mgluizbrito.mediaconduit_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormatInfo {

    @JsonProperty("format_id")
    private String formatId;

    @JsonProperty("ext")
    private String ext;

    @JsonProperty("resolution")
    private String resolution;

    @JsonProperty("vcodec")
    private String vcodec;

    @JsonProperty("acodec")
    private String acodec;

    @JsonProperty("tbr")
    private Double tbr;

    @JsonProperty("url")
    private String formatUrl;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("width")
    private Integer width;
}

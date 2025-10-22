package io.github.mgluizbrito.mediaconduit_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.mgluizbrito.mediaconduit_api.cli.YtDlpDateDeserializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "media_metadata", schema = "public")
public class MediaMetadata {

    @Embedded
    @JsonProperty("formats")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "media_formats", joinColumns = @JoinColumn(name = "metadata_video_source_id"))
    Set<FormatInfo> formats;
    @JsonProperty("id")
    @Id
    @Column(name = "video_source_id", nullable = false)
    private String videoSourceId;
    @JsonProperty("title")
    @Column(name = "title", nullable = false)
    private String title;
    @JsonProperty("duration")
    @Column(name = "duration")
    private Long duration;
    @JsonProperty("upload_date")
    @JsonDeserialize(using = YtDlpDateDeserializer.class)
    @Column(name = "upload")
    private LocalDate upload;
    @JsonProperty("channel_url")
    @Column(name = "channel_url")
    private String channelUrl;
    @JsonProperty("view_count")
    @Column(name = "view_count")
    private BigInteger viewCount;
    @JsonProperty("like_count")
    @Column(name = "like_count")
    private BigInteger likeCount;
    @JsonProperty("description")
    @Column(name = "description")
    private String description;
    @OneToOne(mappedBy = "metadata", fetch = FetchType.LAZY)
    private ExtractionTask task;
}
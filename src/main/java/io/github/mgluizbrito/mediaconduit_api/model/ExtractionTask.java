package io.github.mgluizbrito.mediaconduit_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "media_extraction", schema = "public")
@Getter
@Setter
public class ExtractionTask {

    @Id
    private UUID jobId;

    @Column(name = "media_identifier", unique = true, nullable = false)
    private String mediaIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "extractor", nullable = false)
    private ExtractorsAvailable extractor;

    @Column(name = "file_url")
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status;

    @Column(name = "progress", nullable = false)
    private int progress;

    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "metadata_id")
    private MediaMetadata metadata;
}
package io.github.mgluizbrito.mediaconduit_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.URL;

@Embeddable
@Getter
@Setter
@RequiredArgsConstructor
public class ThumbInfo {

    @Column(name = "thumbnail_url")
    private URL url;

    @Column(name = "thumbnail_width")
    private Integer width;

    @Column(name = "thumbnail_height")
    private Integer height;
}

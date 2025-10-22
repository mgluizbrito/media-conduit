package io.github.mgluizbrito.mediaconduit_api.exceptions;

import lombok.Getter;

@Getter
public class MediaExtractionException extends RuntimeException {

    private final String externalErrorMessage;

    public MediaExtractionException(String message, String externalErrorMessage) {

        super(message + ": " + externalErrorMessage);
        this.externalErrorMessage = externalErrorMessage;
    }
}

package io.github.biopuri.astroplanner.desktop.validation;

/**
 * Exception thrown when desktop form input is invalid.
 */
public class InputValidationException extends RuntimeException {

    public InputValidationException(String message) {
        super(message);
    }
}

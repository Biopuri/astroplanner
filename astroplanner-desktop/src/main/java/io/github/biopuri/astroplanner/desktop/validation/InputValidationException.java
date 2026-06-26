package io.github.biopuri.astroplanner.desktop.validation;

import java.util.List;

/**
 * Exception thrown when a search form field contains invalid input.
 *
 * @author seijime
 */
public class InputValidationException extends RuntimeException {

    private final List<SearchField> fields;

    public InputValidationException(
            String message,
            SearchField... fields
    ) {
        super(message);
        this.fields = List.of(fields);
    }

    /**
     * Returns the invalid search form field.
     *
     * @return invalid fields identifiers.
     */
    public List<SearchField> getFields() {
        return fields;
    }
}

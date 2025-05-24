package br.com.coletaverde.infrastructure.exceptions;

/**
 * Enumeration of business exception messages used in the application.
 */
public enum BusinessExceptionMessage {

    NOT_FOUND("The instance does not exist in the database."),
    ATTRIBUTE_VALUE_ALREADY_EXISTS("The value of attribute %s is already in use."),
    USER_NOT_FOUND_BY_EMAIL("No user found with email: %s");

    private final String message;

    BusinessExceptionMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the raw message string associated with the enumeration constant.
     *
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets a formatted message string only for placeholders.
     *
     * @param args Arguments to format the message.
     * @return The formatted message string.
     * @throws IllegalStateException if message does not contain format specifiers.
     */
    public String format(Object... args) {
        if (!message.contains("%s")) {
            throw new IllegalStateException("This message does not support formatting: " + name());
        }
        return String.format(message, args);
    }
}

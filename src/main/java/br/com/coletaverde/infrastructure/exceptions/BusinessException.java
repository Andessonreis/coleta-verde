package br.com.coletaverde.infrastructure.exceptions;

import java.io.Serial;

public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6062815084191991722L;

    /**
     * BusinessException represents an exception to business rules of the system.
     *
     */
    public BusinessException() {
        super();
    }

    /**
     * Creates a new instance of the exception setting the message.
     *
     * @param message the error message.
     */
    public BusinessException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance of the exception setting the cause.
     *
     * @param cause the cause
     */
    public BusinessException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of the exception setting the message and cause.
     *
     * @param message the error message.
     * @param cause   the cause
     */
    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
package javascalautils;

/**
 * Used when a function throws an exception which cannot be raised. <br>
 * This exception is then created and wrapped around the original exception.
 * @author Peter Nerg
 * @since 1.11
 */
public final class BrokenFunctionException extends RuntimeException {
    private static final long serialVersionUID = 3534543534523L;
    /**
     * Creates the exception
     * @param message The message
     * @param cause The underlying cause
     */
    public BrokenFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

}

package bob.util;

/**
 * Shorthand to specify restrictions and assumptions that must be true.
 */
public class Assert {

    private Assert() {
    }

    /**
     * Validates that the test expression is true, throwing a run time exception if not.
     *
     * @param test The test expression
     * @param msg The message for the thrown exception
     */
    public static void that(boolean test, String msg) {
        if (!test) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * Wraps a thrown exception with a run time exception.
     *
     * @param thrown The exception that was thrown
     * @param msg The message for the thrown exception
     * @return The exception to throw
     */
    public static RuntimeException failed(Throwable thrown, String msg) {
        return new RuntimeException(msg, thrown);
    }
}

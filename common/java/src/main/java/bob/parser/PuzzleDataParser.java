package bob.parser;

/**
 * The parser reads the puzzle data and collects it into a data object for the puzzle solver to use.
 *
 * @param <T> The type of data object being produced
 */
public interface PuzzleDataParser<T> {

    /**
     * Start a new part number (start of file or read a blank line).
     *
     * @param partnum The part number, starting with 0
     */
    default void open(int partnum) {
    }

    /**
     * End a part number (end of file or read a blank line).
     *
     * @param partnum The part number, starting with 0
     */
    default void close(int partnum) {
    }

    /**
     * Parse one line of the puzzle data.
     *
     * @param partnum The part number, starting with 0
     * @param line The line read from the file
     */
    void read(int partnum, String line);

    /**
     * Retrieve the data object resulting from the input file.
     *
     * @return The data object
     */
    T getResult();
}

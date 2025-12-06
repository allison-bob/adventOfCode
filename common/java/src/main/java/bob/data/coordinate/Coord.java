package bob.data.coordinate;

/**
 * A coordinate with some number of dimensions. Valid coordinate values are always non-negative.
 * <p>
 * Two coordinate objects are equal if they contain the same coordinate values.
 * </p>
 *
 * @param <C> This coordinate class
 * @param <D> The coordinate class with one fewer dimensions
 */
public interface Coord<C extends Coord, D extends Coord> extends Comparable<C> {

    /**
     * Add an offset to each coordinate value.
     * @param offset The value to add
     * @return The new coordinate
     */
    C addOffset(int offset);

    /**
     * Add an offset to the coordinate value.
     * @param offset The offset coordinate
     * @return The new coordinate
     */
    C addOffset(C offset);

    /**
     * Retrieve the down-dimension coordinate (dropping the highest-level dimension).
     *
     * @return The coordinate
     */
    D getDownDim();

    /**
     * Retrieve the coordinate value for the highest-level dimension.
     *
     * @return The coordinate value
     */
    int getThisDim();

    /**
     * Verifies that the coordinate values are not negative.
     *
     * @return {@code false} if the coordinate includes a negative value, {@code true} otherwise
     */
    boolean isNotNegative();

    /**
     * Returns the Manhattan distance (sum of the absolute values of the coordinate values) from the origin.
     *
     * @return The Manhattan distance
     */
    int manhattan();

    /**
     * Returns the Manhattan distance (sum of the absolute values of the coordinate values) from the specified location.
     *
     * @param location The location to measure from
     * @return The Manhattan distance
     */
    int manhattan(C location);

    /**
     * Returns the coordinate values as a comma-separated string.
     *
     * @return The coordinate values
     */
    String values();
}

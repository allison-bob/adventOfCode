package bob.data.grid;

import bob.data.coordinate.Coord;

/**
 * Locate a neighbor based on a starting coordinate and direction.
 *
 * @param <T> The type of object at each grid point, often an enum
 * @param <C> The coordinate class to specify coordinates
 */
@FunctionalInterface
public interface NeighborFinder<T, C extends Coord> {

    /**
     * Find the next neighbor in the indicated direction.
     *
     * @param start The starting point for the search
     * @param offset The offset for each search step (coordinate values are usually -1, 0, or 1)
     * @return The object at the neighboring coordinate
     */
    T find(C start, C offset);
}

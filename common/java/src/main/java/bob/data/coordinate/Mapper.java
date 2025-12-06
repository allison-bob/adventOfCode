package bob.data.coordinate;

/**
 * Map coordinates based on various permutations of rotations and flips. Using a Mapper with a grid is incompatible with
 * wrapping coordinates in any direction.
 *
 * <p>
 * Mappers convert between two sets of coordinates:
 * </p>
 * <ul>
 * <li>The original coordinates, which are used while building the grid</li>
 * <li>The mapped coordinates as defined with the particular Mapper instance</li>
 * </ul>
 *
 * @param <C> The coordinate class to specify coordinates
 */
public interface Mapper<C extends Coord> {

    /**
     * Convert an original coordinate to a mapped coordinate.
     *
     * @param origCoord The original coordinate
     * @return The mapped coordinate
     */
    C map(C origCoord);

    /**
     * Convert an mapped coordinate to a original coordinate.
     *
     * @param mapCoord The mapped coordinate
     * @return The original coordinate
     */
    C unmap(C mapCoord);

    /**
     * Convert an original grid size to a mapped grid size. This is implemented by mapping the size as a coordinate then
     * getting the absolute value of each coordinate value.
     *
     * @param origSize The original grid size
     * @return The mapped grid size
     */
    C mapSize(C origSize);
}

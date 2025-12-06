package bob.data.grid;

import bob.data.coordinate.Coord;
import bob.data.coordinate.Mapper;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A grid of points, where each point is in a state represented by an enum.
 * <p>
 * Grids are assumed to have uniform dimensions, meaning all down-dimension grids are the same size.
 * </p>
 *
 * @param <T> The type of object at each grid point, often an enum
 * @param <DG> The grid class to hold down-dimension grids
 * @param <C> The coordinate class to specify coordinates
 * @param <DC> The coordinate class to specify down-dimension coordinates
 */
public interface Grid<T, DG extends GridBase, C extends Coord, DC extends Coord> {

    /**
     * Add a border to the grid. Adds a border to each existing subgrid then adds new subgrids above and below. Any
     * cached coordinates will be invalidated by this operation.
     *
     * @param border The object to add
     */
    void addBorder(T border);

    /**
     * Add a row of points at the bottom of the first layer, ignoring orientation. If the grid consists of more than
     * two dimensions, the new row will be positioned with all higher dimensions equal to zero.
     *
     * @param row The row of points to add
     */
    void addRow(List<T> row);

    /**
     * Add a row of points at the specified position, ignoring orientation. Rows must be added in sequence along all
     * dimensions. The X value of the position is ignored since the supplied row is placed along the X axis.
     *
     * @param position The position for the new row
     * @param row The row of points to add
     */
    void addRow(C position, List<T> row);

    /**
     * Create a string with the contents of the grid in the current orientation with (0,0) at the top-left corner. Each
     * row ends with "\n" so that, when displayed, each row is on a separate line.
     *
     * @param dumper A function to convert a grid object into an object that can be added to a StringBuilder; if the
     * grid object's {@code toString} method produces the correct result, this parameter can be specified as
     * "{@code Function::identity}"
     * @return The string
     */
    String dump(Function<T, Object> dumper);

    /**
     * Retrieve the object at the specified coordinates in the current orientation. Each subclass should have an
     * overload of this method with individual coordinate parameters for easier use in loops, etc.
     *
     * @param mappedPos The desired mapped position
     * @return The object or {@code null} if the coordinate is outside of the grid
     */
    T get(C mappedPos);

    /**
     * Retrieve a coordinate representing the grid's size in each dimension.
     *
     * @return The coordinate
     */
    C getSize();

    /**
     * Retrieve a stream of neighboring grid objects in the current orientation.
     *
     * @param start The starting point
     * @return The stream
     */
    Stream<T> neighborStream(C start);

    /**
     * Retrieve a stream of neighboring grid objects in the current orientation.
     *
     * @param start The starting point
     * @param finder The method to locate the appropriate neighbor
     * @return The stream
     */
    Stream<T> neighborStream(C start, NeighborFinder<T, C> finder);

    /**
     * Retrieve a stream of grid objects in the current orientation. There is no way to determine the coordinate for any
     * object in the stream.
     *
     * @return The stream
     */
    Stream<T> pointStream();

    /**
     * Retrieve a stream of grid rows in the current orientation. There is no way to determine the coordinate for any
     * object in the stream.
     *
     * @return The stream
     */
    Stream<List<T>> rowStream();

    /**
     * Update the object at the specified coordinates in the current orientation. Each subclass should have an
     * overload of this method with individual coordinate parameters for easier use in loops, etc.
     *
     * @param mappedPos The desired mapped position
     * @param newval The new object for the coordinate
     */
    void set(C mappedPos, T newval);

    /**
     * Set the orientation of the grid.
     *
     * @param orientation the orientation to set
     */
    void setOrientation(Mapper<C> orientation);
}

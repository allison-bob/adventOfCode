package bob.data.grid;

import bob.data.coordinate.Coord1D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class maintains a 1D grid of objects.
 *
 * @param <T> The type of object at each grid point
 */
public class Grid1D<T> extends GridBase<T, Grid1D, Grid1D, Coord1D, Coord1D> {

    // Storage for the grid elements
    // NOTE: This class needs to override any GridBase methods that reference subgrids since this class does not store
    // anything in the subgrids list
    private final List<T> points = new ArrayList<>();

    /**
     * Normal constructor. Note that wrapping is not performed unless enabled in Grid2D.
     */
    public Grid1D() {
        super(new Coord1D(0), Grid1D.class, Coord1D.class, Coord1D.class);
    }

    //
    //
    // Methods unique to this class
    //
    /**
     * Retrieve the object at the specified coordinate.
     *
     * @param x The X coordinate
     * @return The object or {@code null} if the coordinate is outside of the grid
     */
    public T get(int x) {
        return get(new Coord1D(x));
    }

    /**
     * Get the list of points.
     *
     * @return The list of points
     */
    List<T> getPoints() {
        return points;
    }
    
    /**
     * Set the object at the specified coordinates.
     *
     * @param x The X coordinate
     * @param newval The new object for the coordinate
     */
    public void set(int x, T newval) {
        set(new Coord1D(x), newval);
    }
    
    private T defaultNeighborFinder(Coord1D start, Coord1D offset) {
        return get(start.getX() + offset.getX());
    }

    //
    //
    // Methods overriding public GridBase methods
    //
    @Override
    public void addBorder(T border) {
        points.add(border);
        points.add(0, border);
    }

    @Override
    public void addRow(List<T> row) {
        points.clear();
        points.addAll(row);
    }

    @Override
    public void addRow(Coord1D position, List<T> row) {
        points.clear();
        points.addAll(row);
    }

    @Override
    public Coord1D getSize() {
        return new Coord1D(points.size());
    }

    @Override
    public Stream<T> neighborStream(Coord1D start) {
        return neighborStream(start, this::defaultNeighborFinder);
    }

    @Override
    public Stream<T> pointStream() {
        return points.stream();
    }

    @Override
    public Stream<List<T>> rowStream() {
        return Stream.of(points);
    }

    //
    //
    // Methods overriding protected GridBase methods
    //
    @Override
    protected void add(Grid1D subgrid) {
        // This method shouldn't do anything
    }

    @Override
    protected Stream<Coord1D> directions() {
        return Stream.of(new Coord1D(-1), new Coord1D(0), new Coord1D(1));
    }

    @Override
    protected void dumpGrid(StringBuilder sb, String prefix, Function<T, Object> dumper) {
        for (int x = 0; x < points.size(); x++) {
            sb.append(dumper.apply(points.get(x)));
        }
        sb.append("\n");
    }

    @Override
    protected T getInternal(Coord1D location, Coord1D wrap) {
        int pos = location.getThisDim();

        // Undo any wrapping of this dimension
        if (wrap.getThisDim() == 1) {
            pos = pos % points.size();
        }

        // Ensure we are still within the grid
        if ((pos < 0) || (pos >= points.size())) {
            return null;
        }

        // Extract the object
        return points.get(pos);
    }

    @Override
    protected Grid1D newBorderGrid(T border) {
        Grid1D retval = new Grid1D();
        for (int i = 0; i < points.size(); i++) {
            retval.points.add(border);
        }
        return retval;
    }

    @Override
    protected void setInternal(Coord1D location, Coord1D wrap, T newval) {
        int pos = location.getThisDim();

        // Undo any wrapping of this dimension
        if (wrap.getThisDim() == 1) {
            pos = pos % points.size();
        }

        // Ensure we are still within the grid
        if ((pos < 0) || (pos >= points.size())) {
            return;
        }

        // Extract the object
        points.set(pos, newval);
    }
}

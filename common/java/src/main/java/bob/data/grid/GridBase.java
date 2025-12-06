package bob.data.grid;

import bob.data.coordinate.Coord;
import bob.data.coordinate.Mapper;
import bob.util.Assert;
import bob.util.ObjectBuilder;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Base class for all grid classes.
 *
 * @param <T> The type of object at each grid point
 * @param <G> The grid class to hold grids
 * @param <DG> The grid class to hold down-dimension grids
 * @param <C> The coordinate class to specify coordinates
 * @param <DC> The coordinate class to specify down-dimension coordinates
 */
public class GridBase<T, G extends GridBase, DG extends GridBase, C extends Coord, DC extends Coord>
        implements Grid<T, DG, C, DC> {

    // Constructors for objects
    private final Constructor<C> cConstruct;
    private final Constructor<G> gConstruct;
    private final Constructor<DG> dgConstruct;

    // Subgrids
    private final List<DG> subgrids = new ArrayList<>();

    // Wrap state for coordinates
    private final C wrap;

    // Grid orientation
    private Mapper<C> orientation;

    /**
     * Create the base class instance.
     *
     * @param wrap The wrap status for all dimensions (0=no wrap, 1=wrap)
     * @param dgClass Class reference for the grid class to hold down-dimension grids
     * @param cClass Class reference for the coordinate class to specify coordinates
     * @param dcClass Class reference for the coordinate class to specify down-dimension coordinates
     */
    public GridBase(C wrap, Class<DG> dgClass, Class<C> cClass, Class<DC> dcClass) {
        this.wrap = wrap;
        this.cConstruct = ObjectBuilder.getConstructor(cClass, dcClass, int.class);
        this.gConstruct = ObjectBuilder.getConstructor((Class<G>) this.getClass());
        this.dgConstruct = ObjectBuilder.getConstructor(dgClass);
    }

    //
    //
    // Implementation of Grid interface
    //
    @Override
    public void addBorder(T border) {
        // Add to each subgrid
        for (DG g : subgrids) {
            g.addBorder(border);
        }

        // Create new first and last entry
        subgrids.add(0, (DG) subgrids.get(0).newBorderGrid(border));
        subgrids.add((DG) subgrids.get(0).newBorderGrid(border));
    }

    @Override
    public void addRow(List<T> row) {
        // Start a new subgrid if required
        if (subgrids.isEmpty()) {
            subgrids.add(ObjectBuilder.getInstance(dgConstruct));
        }
        subgrids.get(0).addRow(row);
    }

    @Override
    public void addRow(C position, List<T> row) {
        int thisDim = position.getThisDim();

        // Validate that the position for this dimension is valid
        Assert.that((thisDim >= 0), "Negative positions are not valid");
        if (subgrids.isEmpty()) {
            Assert.that((thisDim == 0), "Position for first row must be 0");
        } else {
            Assert.that((thisDim <= subgrids.size()), "Position value too large");
        }

        // Start a new subgrid if required
        if (thisDim == subgrids.size()) {
            subgrids.add(ObjectBuilder.getInstance(dgConstruct));
        }
        subgrids.get(thisDim).addRow(position.getDownDim(), row);
    }

    @Override
    public String dump(Function<T, Object> dumper) {
        G mappedGrid = reorient();
        StringBuilder sb = new StringBuilder();
        mappedGrid.dumpGrid(sb, "", dumper);
        return sb.toString();
    }

    @Override
    public T get(C mappedPos) {
        if (!mappedPos.isNotNegative()) {
            // At least one coordinate value is negative
            return null;
        }
        C position = mappedPos;
        if (orientation != null) {
            position = unmapCoord(mappedPos, getSize());
        }
        return getInternal(position, wrap);
    }

    @Override
    public C getSize() {
        return ObjectBuilder.getInstance(cConstruct, subgrids.get(0).getSize(), subgrids.size());
    }

    @Override
    public Stream<T> neighborStream(C start) {
        return Stream.of();
    }

    @Override
    public Stream<T> neighborStream(C start, NeighborFinder<T, C> finder) {
        return directions()
                .filter(c -> c.manhattan() > 0)
                .map(c -> finder.find(start, c));
    }

    @Override
    public Stream<T> pointStream() {
        G mappedGrid = reorient();
        Stream<DG> dgs = mappedGrid.getSubgrids().stream();
        return dgs.flatMap(DG::pointStream);
    }

    @Override
    public Stream<List<T>> rowStream() {
        G mappedGrid = reorient();
        Stream<DG> dgs = mappedGrid.getSubgrids().stream();
        return dgs.flatMap(DG::rowStream);
    }

    @Override
    public void set(C mappedPos, T newval) {
        if (!mappedPos.isNotNegative()) {
            // At least one coordinate value is negative
            return;
        }
        C position = mappedPos;
        if (orientation != null) {
            position = unmapCoord(mappedPos, getSize());
        }
        setInternal(position, wrap, newval);
    }

    @Override
    public void setOrientation(Mapper<C> orientation) {
        this.orientation = orientation;
    }

    //
    //
    // Protected methods to support subclasses
    //
    /**
     * Add a subgrid while building a border.
     *
     * @param subgrid The subgrid to add
     */
    protected void add(DG subgrid) {
        subgrids.add(subgrid);
    }

    /**
     * Build a stream of coordinates representing all directions in this grid. The result also includes an entry with
     * all coordinate values being 0.
     *
     * @return The stream of coordinates
     */
    protected Stream<C> directions() {
        return Stream.of(-1, 0, 1)
                .flatMap(d -> directionSlice(d));
    }

    private Stream<C> directionSlice(int d) {
        return subgrids.get(0).directions()
                .map(dc -> ObjectBuilder.getInstance(cConstruct, dc, d));
    }

    /**
     * Dump the grid. Each grid implementation should contain an override of this method to handle dumping itself,
     * usually by dumping each subgrid in order.
     *
     * @param sb The StringBuilder to append content to
     * @param prefix A prefix to prepend to each subgrid's dump
     * @param dumper A function to convert a grid object into an object that can be added to a StringBuilder
     */
    protected void dumpGrid(StringBuilder sb, String prefix, Function<T, Object> dumper) {
        sb.append("dumpGrid not overridden");
    }

    /**
     * Extract the object by calling this method on the correct subgrid element.
     *
     * @param location The coordinate to retrieve
     * @param wrap The wrap status for all dimensions (0=no wrap, 1=wrap)
     * @return The object
     */
    protected T getInternal(C location, C wrap) {
        int pos = location.getThisDim();

        // Undo any wrapping of this dimension
        if (wrap.getThisDim() == 1) {
            pos = pos % subgrids.size();
        }

        // Ensure we are still within the grid
        if ((pos < 0) || (pos >= subgrids.size())) {
            return null;
        }

        // Extract the object from the subgrid
        return (T) subgrids.get(pos).getInternal(location.getDownDim(), wrap.getDownDim());
    }

    /**
     * Get the orientation of the grid.
     *
     * @return The grid orientation
     */
    protected Mapper<C> getOrientation() {
        return orientation;
    }

    /**
     * Get the list of subgrids.
     *
     * @return The list of subgrids
     */
    List<DG> getSubgrids() {
        return subgrids;
    }

    /**
     * Build a grid the same size as this one but with the specified border object at every point.
     *
     * @param border The border value to use
     * @return The grid
     */
    protected G newBorderGrid(T border) {
        G retval = ObjectBuilder.getInstance(gConstruct);
        C size = getSize();
        for (int i = 0; i < size.getThisDim(); i++) {
            retval.add(subgrids.get(0).newBorderGrid(border));
        }
        return retval;
    }

    /**
     * Build the mapped grid for the current orientation. This default method does not do any mapping.
     *
     * @return The mapped grid
     */
    protected G reorient() {
        return (G) this;
    }

    /**
     * Set the object by calling this method on the correct subgrid element.
     *
     * @param location The coordinate to retrieve
     * @param wrap The wrap status for all dimensions (0=no wrap, 1=wrap)
     * @param newval The new object for the coordinate
     */
    protected void setInternal(C location, C wrap, T newval) {
        int pos = location.getThisDim();

        // Undo any wrapping of this dimension
        if (wrap.getThisDim() == 1) {
            pos = pos % subgrids.size();
        }

        // Ensure we are still within the grid
        if ((pos < 0) || (pos >= subgrids.size())) {
            return;
        }

        // Extract the object from the subgrid
        subgrids.get(pos).setInternal(location.getDownDim(), wrap.getDownDim(), newval);
    }

    /**
     * Unmap the coordinate based on the current orientation. This default implementation does not use variable
     * orientations, so simply returns the provided coordinate.
     *
     * @param mappedPos The mapped coordinate
     * @param origSize The original grid size
     * @return The original coordinate
     */
    protected C unmapCoord(C mappedPos, C origSize) {
        return mappedPos;
    }
}

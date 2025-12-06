package bob.data.grid;

import bob.data.coordinate.Coord1D;
import bob.data.coordinate.Coord2D;
import bob.data.coordinate.Mapper;
import bob.data.coordinate.Mapper2D;
import bob.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class maintains a 2D grid of objects.
 *
 * @param <T> The type of object at each grid point
 */
public class Grid2D<T> extends GridBase<T, Grid2D, Grid1D, Coord2D, Coord1D> {

    private final boolean wrapping;

    /**
     * Constructor used when this grid is a subgrid. Note that wrapping is not performed.
     */
    public Grid2D() {
        this(false, false);
    }

    /**
     * Normal constructor. Wrapping is controlled by the parameters.
     *
     * @param wrapX {@code true} to enable wrapping along the X axis
     * @param wrapY {@code true} to enable wrapping along the Y axis
     */
    public Grid2D(boolean wrapX, boolean wrapY) {
        super(new Coord2D((wrapX ? 1 : 0), (wrapY ? 1 : 0)), Grid1D.class, Coord2D.class, Coord1D.class);
        wrapping = wrapX || wrapY;
    }

    //
    //
    // Methods unique to this class
    //
    public void addBorderIf(T border) {
        // Verify that all border points are the specified border object
        boolean allBorder = borderStream().allMatch(p -> p.equals(border));
        if (!allBorder) {
            addBorder(border);
        }
    }

    public Stream<T> borderStream() {
        List<T> items = new ArrayList<>();
        int xmax = getSize().getX() - 1;
        int ymax = getSize().getY() - 1;
        for (int i = 0; i < xmax; i++) {
            items.add(get(i, 0));
        }
        for (int i = 0; i < ymax; i++) {
            items.add(get(xmax, i));
        }
        for (int i = 0; i < xmax; i++) {
            items.add(get((xmax - i), ymax));
        }
        for (int i = 0; i < ymax; i++) {
            items.add(get(0, (ymax - i)));
        }
        return items.stream();
    }

    /**
     * Return the neighbors along each compass point.
     *
     * @param start The starting point
     * @return A stream of neighboring entries
     */
    public Stream<T> compassNeighborStream(Coord2D start) {
        return compassNeighborStream(start, this::defaultNeighborFinder);
    }

    /**
     * Return the neighbors along each compass point.
     *
     * @param start The starting point
     * @param finder The method to locate the appropriate neighbor
     * @return A stream of neighboring entries
     */
    public Stream<T> compassNeighborStream(Coord2D start, NeighborFinder<T, Coord2D> finder) {
        return Stream.of(new Coord2D(1, 0), new Coord2D(-1, 0), new Coord2D(0, 1), new Coord2D(0, -1))
                .map(c -> finder.find(start, c));
    }
    
    public void fill(int xSize, int ySize, T fill) {
        List<T> row = new ArrayList<>();
        for (int x = 0; x < xSize; x++) {
            row.add(fill);
        }
        for (int y = 0; y < ySize; y++) {
            addRow(row);
        }
    }

    /**
     * Retrieve the object at the specified coordinates in the current orientation.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @return The object or {@code null} if the coordinate is outside of the grid
     */
    public T get(int x, int y) {
        return get(new Coord2D(x, y));
    }
    
    /**
     * Set the object at the specified coordinates.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param newval The new object for the coordinate
     */
    public void set(int x, int y, T newval) {
        set(new Coord2D(x, y), newval);
    }

    private T defaultNeighborFinder(Coord2D start, Coord2D offset) {
        return get((start.getX() + offset.getX()), (start.getY() + offset.getY()));
    }

    //
    //
    // Methods overriding public GridBase methods
    //
    @Override
    public void addRow(List<T> row) {
        Grid1D newrow = new Grid1D();
        newrow.addRow(row);
        getSubgrids().add(newrow);
    }

    @Override
    public Stream<T> neighborStream(Coord2D start) {
        return neighborStream(start, this::defaultNeighborFinder);
    }

    @Override
    public void setOrientation(Mapper<Coord2D> orientation) {
        Assert.that((!wrapping), "orientation change not supported if wrapping");
        super.setOrientation((orientation == Mapper2D.EN) ? null : orientation);
    }

    //
    //
    // Methods overriding protected GridBase methods
    //
    @Override
    protected void dumpGrid(StringBuilder sb, String prefix, Function<T, Object> dumper) {
        List<Grid1D> grids = getSubgrids();
        for (int y = 0; y < grids.size(); y++) {
            grids.get(y).dumpGrid(sb, "", dumper);
        }
    }

    @Override
    protected Grid2D reorient() {
        if (getOrientation() == null) {
            return this;
        }
        Coord2D mapSize = getOrientation().mapSize(getSize());
        Grid2D result = new Grid2D();
        for (int y = 0; y < mapSize.getY(); y++) {
            Coord2D loc = new Coord2D(0, y);
            List<T> row = new ArrayList<>();
            for (int x = 0; x < mapSize.getX(); x++) {
                row.add(get(x, y));
            }
            result.addRow(loc, row);
        }
        return result;
    }

    @Override
    protected Coord2D unmapCoord(Coord2D mappedPos, Coord2D origSize) {
        // ***** Example params: (1, 2), (4, 3), orientation = WS
        int lenX = origSize.getX();
        int lenY = origSize.getY();

        // To be able to discern if either axis has flipped, we need to add 1 to each coordinate
        // while remapping to eliminate the 0
        Coord2D adjusted = new Coord2D((mappedPos.getX() + 1), (mappedPos.getY() + 1));
        // ***** adjusted = (2, 3)

        // Map the coordinates
        Coord2D origPos = getOrientation().unmap(adjusted);
        int x = origPos.getX();
        int y = origPos.getY();
        // ***** origPos = (-2, -3)

        // Handle negative coordinates due to mapping
        if (x < 0) {
            while (x < 0) {
                x += lenX;
            }
            x++;
            // ***** x = -2 + 4 + 1 = 3
        }
        if (y < 0) {
            while (y < 0) {
                y += lenY;
            }
            y++;
            // ***** y = -3 + 3 + 1 = 1
        }

        // Remove the temporary +1 added above
        return new Coord2D((x - 1), (y - 1));
        // ***** return (2, 0)
    }
}

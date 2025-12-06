package bob.data.grid;

import bob.data.coordinate.Coord3D;
import bob.data.coordinate.Coord4D;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class maintains a 4D grid of objects.
 * 
 * @param <T> The type of object at each grid point
 */
public class Grid4D<T> extends GridBase<T, Grid4D, Grid3D, Coord4D, Coord3D> {

    /**
     * Normal constructor. Note that wrapping is not performed.
     */
    public Grid4D() {
        super(new Coord4D(0, 0, 0, 0), Grid3D.class, Coord4D.class, Coord3D.class);
    }

    //
    //
    // Methods unique to this class
    //
    /**
     * Retrieve the object at the specified coordinates.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param w The W coordinate
     * @return The object or {@code null} if the coordinate is outside of the grid
     */
    public T get(int x, int y, int z, int w) {
        return get(new Coord4D(x, y, z, w));
    }
    
    /**
     * Set the object at the specified coordinates.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param w The W coordinate
     * @param newval The new object for the coordinate
     */
    public void set(int x, int y, int z, int w, T newval) {
        set(new Coord4D(x, y, z, w), newval);
    }
    
    private T defaultNeighborFinder(Coord4D start, Coord4D offset) {
        return get((start.getX() + offset.getX()), (start.getY() + offset.getY()),
                (start.getZ() + offset.getZ()), (start.getW() + offset.getW()));
    }

    //
    //
    // Methods overriding public GridBase methods
    //
    @Override
    public Stream<T> neighborStream(Coord4D start) {
        return neighborStream(start, this::defaultNeighborFinder);
    }
    
    //
    //
    // Methods overriding protected GridBase methods
    //
    @Override
    protected void dumpGrid(StringBuilder sb, String prefix, Function<T, Object> dumper) {
        List<Grid3D> grids = getSubgrids();
        for (int w = 0; w < grids.size(); w++) {
            grids.get(w).dumpGrid(sb, "w = " + w + ", ", dumper);
        }
    }
}

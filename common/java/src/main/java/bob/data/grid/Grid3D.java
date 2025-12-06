package bob.data.grid;

import bob.data.coordinate.Coord2D;
import bob.data.coordinate.Coord3D;
import bob.data.coordinate.Mapper;
import bob.data.coordinate.Mapper3D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class maintains a 3D grid of objects.
 *
 * @param <T> The type of object at each grid point
 */
public class Grid3D<T> extends GridBase<T, Grid3D, Grid2D, Coord3D, Coord2D> {

    /**
     * Normal constructor. Note that wrapping is not performed.
     */
    public Grid3D() {
        super(new Coord3D(0, 0, 0), Grid2D.class, Coord3D.class, Coord2D.class);
    }

    //
    //
    // Methods unique to this class
    //
    /**
     * Retrieve the object at the specified coordinates in the current orientation.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @return The object or {@code null} if the coordinate is outside of the grid
     */
    public T get(int x, int y, int z) {
        return get(new Coord3D(x, y, z));
    }
    
    /**
     * Set the object at the specified coordinates.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param newval The new object for the coordinate
     */
    public void set(int x, int y, int z, T newval) {
        set(new Coord3D(x, y, z), newval);
    }
    
    private T defaultNeighborFinder(Coord3D start, Coord3D offset) {
        return get((start.getX() + offset.getX()), (start.getY() + offset.getY()), (start.getZ() + offset.getZ()));
    }

    //
    //
    // Methods overriding public GridBase methods
    //
    @Override
    public Stream<T> neighborStream(Coord3D start) {
        return neighborStream(start, this::defaultNeighborFinder);
    }

    @Override
    public void setOrientation(Mapper<Coord3D> orientation) {
        super.setOrientation((orientation == Mapper3D.ENU) ? null : orientation);
    }

    //
    //
    // Methods overriding protected GridBase methods
    //
    @Override
    protected void dumpGrid(StringBuilder sb, String prefix, Function<T, Object> dumper) {
        List<Grid2D> grids = getSubgrids();
        for (int z = 0; z < grids.size(); z++) {
            sb.append(prefix).append("z = ").append(z).append("\n");
            grids.get(z).dumpGrid(sb, "", dumper);
            sb.append("\n");
        }
    }

    @Override
    protected Grid3D reorient() {
        if (getOrientation() == null) {
            return this;
        }
        Coord3D mapSize = getOrientation().mapSize(getSize());
        Grid3D result = new Grid3D();
        for (int z = 0; z < mapSize.getZ(); z++) {
            for (int y = 0; y < mapSize.getY(); y++) {
                Coord3D loc = new Coord3D(0, y, z);
                List<T> row = new ArrayList<>();
                for (int x = 0; x < mapSize.getX(); x++) {
                    row.add(get(x, y, z));
                }
                result.addRow(loc, row);
            }
        }
        return result;
    }

    @Override
    protected Coord3D unmapCoord(Coord3D mappedPos, Coord3D origSize) {
        // ***** Example params: (2, 0, 1), (4, 2, 3), orientation = WDS
        int lenX = origSize.getX();
        int lenY = origSize.getY();
        int lenZ = origSize.getZ();

        // To be able to discern if either axis has flipped, we need to add 1 to each coordinate
        // while remapping to eliminate the 0
        Coord3D adjusted = new Coord3D((mappedPos.getX() + 1), (mappedPos.getY() + 1), (mappedPos.getZ() + 1));
        // ***** adjusted = (3, 1, 2)

        // Map the coordinates
        Coord3D origPos = getOrientation().unmap(adjusted);
        int x = origPos.getX();
        int y = origPos.getY();
        int z = origPos.getZ();
        // ***** origPos = (-3, -1, -2)

        // Handle negative coordinates due to mapping
        if (x < 0) {
            while (x < 0) {
                x += lenX;
            }
            x++;
            // ***** x = -3 + 4 + 1 = 2
        }
        if (y < 0) {
            while (y < 0) {
                y += lenY;
            }
            y++;
            // ***** y = -1 + 2 + 1 = 2
        }
        if (z < 0) {
            while (z < 0) {
                z += lenZ;
            }
            z++;
            // ***** z = -2 + 3 + 1 = 2
        }

        // Remove the temporary +1 added above
        return new Coord3D((x - 1), (y - 1), (z - 1));
        // ***** return (1, 1, 1)
    }
}
